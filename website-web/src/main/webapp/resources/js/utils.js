function setTheme(name) {
setCookie('clienttheme',name);
window.location.reload();
}
function setLanguage(name) {
setCookie('clientlanguage',name);
window.location.reload();
}
function setCookie(name,value){   
var Days = 30;
var exp  = new Date();
exp.setTime(exp.getTime() + Days*24*60*60*1000);
document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}