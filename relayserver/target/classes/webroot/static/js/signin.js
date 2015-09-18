
$(document).ready(function () {
  var isSavedId = getCookie('saveId');
  var savedId  = getCookie('id');
  if(isSavedId) {
      $('#chk-save-id').attr('checked', true);
      $('#txt-user-id').val(savedId);
}

$('#frm-signin').submit(function (e) {
//alert();
  if($('#chk-save-id').is(':checked')) {
      setCookie('saveId', 'true', 10000000);
      setCookie('id', $('#txt-user-id').val(), 10000000);
  }
  return formRequest(this, onReqSuccess, onReqFailed);
  });
});
function onReqSuccess(data) {
  if(data.result) {
  //modalAlert('success');
  location.replace('/projectmain');
  } else {
  modalAlert(signinTitle, signinERRmsg+'<br>'+tryAgain);
  }
}
function onReqFailed(data) {
  var res = $.parseJSON(data.responseText);
  modalAlert('', res.message);
}