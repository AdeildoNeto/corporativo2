/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


jQuery(function () {
    jQuery.noConflict();
});
onload = startApp;

var googleUser = {};

function startApp() {
    gapi.load('auth2', function () {
        auth2 = gapi.auth2.init({
            client_id: '131562098478-bvjjnubvmsauka865rsd8rrdol9flj9n.apps.googleusercontent.com',
            cookiepolicy: 'single_host_origin'
        });
        attachSignin(document.getElementById('btnGoogleLogin'));
    });
}
;

function attachSignin(element) {
    console.log(element.id);
    auth2.attachClickHandler(element, {},
            function (googleUser) {
                onSignIn(googleUser);
            },
            function (error) {
                alert(JSON.stringify(error, undefined, 2));
            });
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}

function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();

    var id_token = googleUser.getAuthResponse().id_token;

    login([{name: 'token', value: id_token}]);
}

function handleComplete(xhr, status, args) {
    var nomeDoAtributo = args.logou;
    alert(nomeDoAtributo);
}