const picture1 = document.getElementById('ca-images');
const picture2 = document.getElementById('comment-part');

// Get references to the parent containers
const container1 = document.getElementById('post-pic');
const container2 = document.getElementById('comment-place');

// Get reference to the button


function executeFunction(){
console.log("Screen width is smaller than 769px ,executing function");

// Add click event listener to the button
window.addEventListener('resize', function() {
// Remove the first picture (No.1 picture) from container1
container1.removeChild(picture1);

// Remove the second picture (No.2 picture) from container2
container2.removeChild(picture2);

// Append the first picture (No.1 picture) to container2
container2.appendChild(picture1);

// Append the second picture (No.2 picture) to container1
container1.appendChild(picture2);
});

}
function executeFunctions(){
console.log("Screen width is bigller than 769px ,executing function");

// Add click event listener to the button
window.addEventListener('resize', function() {
// Remove the first picture (No.1 picture) from container1

container2.removeChild(picture1);

// Remove the second picture (No.2 picture) from container2
container1.removeChild(picture2);

// Append the first picture (No.1 picture) to container2
container1.appendChild(picture1);

// Append the second picture (No.2 picture) to container1
container2.appendChild(picture2);
});
}
function checkScreenWidth(){
const screenWidth = window.innerWidth;
if(screenWidth<769){
    executeFunction();
}
if(screenWidth>769){
    executeFunctions();
}
}
window.addEventListener('resize',checkScreenWidth);

checkScreenWidth();