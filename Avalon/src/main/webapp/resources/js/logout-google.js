/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var auth2;

onload = initClient;

function initClient () {
    gapi.load('auth2', function(){
        auth2 = gapi.auth2.init({
            client_id: '131562098478-bvjjnubvmsauka865rsd8rrdol9flj9n.apps.googleusercontent.com'
        });
    });
};

function signOut() {
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}
