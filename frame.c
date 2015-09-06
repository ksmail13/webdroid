#include <stdio.h>
#include <errno.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <linux/fb.h>
#include <unistd.h>
#include <string.h>
#include <jpeglib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define BUF_SIZE 1024

int getFrameBuffer (int pixelformat, unsigned char **buf);
void write_JPEG_buffer(unsigned char ** outbuffer, unsigned long * outsize, int w, int h, JSAMPLE *buf);

static struct fb_var_screeninfo v_info;
static struct fb_fix_screeninfo	f_info;
int fd;

int main()
{
    int size;
    unsigned char* buf;
    unsigned char* cur;
    
    int sock_fd;
    int addr_len;
    int ret;
    int left;
    int i = 1;
    
	unsigned long outsize = 0;
	unsigned char* outbuf = NULL;
    //const int READ_BYTE = WIDTH * HEIGHT * BYTE_PER_PIXEL * SCREEN_NUM;

    struct sockaddr_in server_addr;
    //set sock_fd and server_addr
    addr_len = sizeof(server_addr);
    sock_fd = socket(PF_INET, SOCK_STREAM, 0);
    if (sock_fd < 0)
    {
        perror("socket error : ");
        exit(0);
    }
    memset( &server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr("211.243.108.156");
    server_addr.sin_port = htons(5554);
    
    //connect server
    if(connect(sock_fd, (struct sockaddr*)&server_addr, addr_len) == -1){
        printf("Connect Error!!\n");
        return 0;
    }


    // open fb0
    fd = open("/dev/fb0", O_RDONLY);

    if(fd<0)
    {
        printf("open fail!\n");
        printf("%s\n",strerror(errno));
        return errno;
    }
    else{
        printf("open complete\n");
    }

    ret = ioctl(fd,FBIOGET_VSCREENINFO,&v_info);
    if(ret < 0){
        printf("Canot open Variable screen Info\n");
        close(fd);
        return 0;
    }
    else{
        printf("open Variable screen Info Complete\n");
    }       
    
    ret = ioctl(fd,FBIOGET_FSCREENINFO,&f_info);
    if(ret < 0){
        printf("Cannot open fixed screen Info\n");
        close(fd);
        return 0;
    }
    else{
        printf("open fixed screen info Complete\n");
    }
    
    size = v_info.xres * v_info.yres * v_info.bits_per_pixel;
    
    printf("x : %d, y : %d, bits/pix : %d\n",v_info.xres,v_info.yres,v_info.bits_per_pixel);
    // get pointer
   
    printf("%d,%d\n",f_info.smem_len,size);    	
    getFrameBuffer (1, &buf);
	printf("write jpeg start!\n");
	write_JPEG_buffer(&outbuf, &outsize, v_info.xres, v_info.yres, buf);
	printf("outbuffer = %x, outsize = %d\n", outbuf, outsize);
	cur = outbuf;
	left = outsize;
	while(left > 0){
	    
	    if(left > BUF_SIZE){
	        printf("%d ,", i++);
            write(sock_fd,cur,BUF_SIZE);
        }
        else{
            printf("%d ,", i++);
            write(sock_fd,cur,left);
        }
        cur += BUF_SIZE;
        left -= BUF_SIZE;
        
    }
    printf("send complete\n");
    
    close(fd);
    
    return 0;
}

int getFrameBuffer (int pixelformat, unsigned char **buf){
	int	i,j,k;
	int	Dst;
	int	ret;
	int size;

	unsigned char *pFrame, *cur;
	int	*intbuf, *intpFrame0, *intpFrame1;
	uint16_t *int16_buf, *int16_pFrame0, *int16_pFrame1;

	uint16_t int16_pixel;
	unsigned char r5, g6, b5, r8, g8, b8;

	printf("[%dx%dx%d]\n", v_info.xres, v_info.yres, v_info.bits_per_pixel);

    size = v_info.xres*v_info.yres*v_info.bits_per_pixel/8;
    
	pFrame = (unsigned char *)mmap(0, f_info.smem_len, PROT_READ, MAP_PRIVATE, fd,0);
	if(pFrame == MAP_FAILED){
		close(fd);
		printf("Map error!\n");
		return -1;
	}
	intpFrame0	= (int *) pFrame;
	intpFrame1	= (int *) (pFrame+f_info.smem_len/2);
	int16_pFrame0 = (uint16_t *) pFrame;
	int16_pFrame1 = (uint16_t *) (pFrame+f_info.smem_len/2);

	*buf = (unsigned char *) malloc(size*3/2);
	cur = *buf;

	intbuf	= (int *) *buf;
	int16_buf	= (uint16_t *)*buf;
	
	
	for(i=0; i < v_info.yres ; i++){
		for(j=0; j < v_info.xres ; j++){
			int16_pixel = *(int16_pFrame0+(i*v_info.xres)+j);
			r5 = (int16_pixel & 0xF800) >> 11;
			g6 = (int16_pixel & 0x07E0) >> 5;
			b5 = (int16_pixel & 0x001F);
			r8 = (r5 << 3) | (r5 >> 2);
			g8 = (g6 << 2) | (g6 >> 4);
			b8 = (b5 << 3) | (b5 >> 2);
			*cur = r8; cur++;
			*cur = g8; cur++;
			*cur = b8; cur++;
		}
	}
	
    /*
	for(i=0; i<v_info.yres; i++){
		for(j=0; j<v_info.xres; j++){
			k = *(int16_pFrame0+(i*v_info.xres*4)+(j*2));
			*(int16_buf+(i*v_info.xres)+j) = k;
		}
	}
	
    /*
	else
	if(frameinfo.pixelFormat == PIXEL_RGBA_5551){
		for(i=0; i<frameinfo.height; i++){
			for(j=0; j<frameinfo.width; j++){
				k = *(intpFrame0+(i*frameinfo.width*4)+(j*2));
				Dst = k & 0xff00ff00;
				Dst += (k & 0x00ff0000) >> 16;
				Dst += (k & 0x000000ff) << 16;
				*(intbuf+(i*frameinfo.width)+j) = Dst;
			}
		}
	}

	else{
		return FORMAT_VALUE_FAIL;
	}
    */
	//*buf = (unsigned char *) int16_buf;

	munmap(pFrame,f_info.smem_len);
	close(fd);

	return j;
}
void write_JPEG_buffer(unsigned char ** outbuffer, unsigned long * outsize, int w, int h, JSAMPLE *buf)
{
	struct jpeg_compress_struct cinfo;
	struct jpeg_error_mgr jerr;

	JSAMPROW row_pointer[1];
	int row_stride;

	cinfo.err = jpeg_std_error(&jerr);
	jpeg_create_compress(&cinfo);

	jpeg_mem_dest(&cinfo, outbuffer, outsize);
    printf("mem_dest complete\n");
	cinfo.image_width = w;
	cinfo.image_height = h;
	cinfo.input_components = 3;
	cinfo.in_color_space = JCS_RGB;

    
	jpeg_set_defaults(&cinfo);
    printf("set_defaults complete\n");
	jpeg_set_quality(&cinfo, 9 * 10, TRUE);
    printf("set_quality complete\n");
	jpeg_start_compress(&cinfo, TRUE);
    printf("start_compress\n");
	row_stride = w * 3;
    printf("row_stride:%d\n",row_stride);
	while(cinfo.next_scanline < cinfo.image_height) {
	    //printf("size : %d\n",sizeof(buf));
		row_pointer[0] = &buf[cinfo.next_scanline * row_stride];
		//printf("asb: %d\n",cinfo.next_scanline*row_stride);
		jpeg_write_scanlines(&cinfo, row_pointer, 1);
		
	}

	jpeg_finish_compress(&cinfo);

	jpeg_destroy_compress(&cinfo);
}

        
