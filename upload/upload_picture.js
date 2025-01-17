function displayImage(event) {
    const file = event.target.files[0]; // Get the selected file
    const reader = new FileReader(); // Create a FileReader to read the file

    // When the file is successfully read, set the image source
    reader.onload = function(e) {
        const imgElement = document.getElementById('uploaded-image');
        imgElement.src = e.target.result; // Set the image source to the file
        imgElement.style.display = 'block'; // Show the image
    };

    if (file) {
        reader.readAsDataURL(file); // Read the image file as a Data URL
    }
}