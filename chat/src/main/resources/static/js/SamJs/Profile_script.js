function toggleName() {
   var blurName = document.getElementById('blurName');
   blurName.classList.toggle('active');
   var popupName = document.getElementById('popupName');
   popupName.classList.toggle('active');
}

function togglePerson() {
   var blurPerson = document.getElementById('blurPerson');
   blurPerson.classList.toggle('active');
   var popupPerson = document.getElementById('popupPerson');
   popupPerson.classList.toggle('active');

   // 加入跳轉功能
   window.location.href = "http://127.0.0.1:5501/8_Setting/Setting.html"; // 將此替換為目標網址
}

function toggleSave() {
   var blurSave = document.getElementById('blurSave');
   blurSave.classList.toggle('active');
   var popupSave = document.getElementById('popupSave');
   popupSave.classList.toggle('active');
}

function togglePost() {
   var blurPost = document.getElementById('blurPost');
   blurPost.classList.toggle('active');
   var popupPost = document.getElementById('popupPost');
   popupPost.classList.toggle('active');
}

function toggleFans() {
   var blurFans = document.getElementById('blurFans');
   blurFans.classList.toggle('active');
   var popupFans = document.getElementById('popupFans');
   popupFans.classList.toggle('active');
}

function toggleFollow() {
   var blurFollow = document.getElementById('blurFollow');
   blurFollow.classList.toggle('active');
   var popupFollow = document.getElementById('popupFollow');
   popupFollow.classList.toggle('active');
}


