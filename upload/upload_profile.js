let cropper; // To store the Cropper instance

// Load and display the uploaded image
function loadImage(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        const imgElement = document.getElementById('uploaded-image');
        imgElement.src = e.target.result;
        imgElement.style.display = 'block'; // Show the image
        initCropper(); // Initialize the cropper after image is loaded
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}

// Initialize Cropper.js on the uploaded image
function initCropper() {
    const img = document.getElementById('uploaded-image');
    if (cropper) {
        cropper.destroy(); // Destroy any previous cropper instance
    }
    cropper = new Cropper(img, {
        aspectRatio: NaN, // Freeform crop (no fixed aspect ratio)
        viewMode: 1, // Show a restricted area for cropping
        autoCropArea: 0.5, // Default cropping area (50%)
        scalable: true, // Allow scaling of the image
        cropBoxResizable: true, // Allow resizing the crop box

    });
}

// Crop the image based on the selected area and resize the entire output image
function cropAndResizeImage() {
    if (cropper) {
        // Get the cropped area (only the selected part of the image)
        const croppedCanvas = cropper.getCroppedCanvas({
            width: 100, // Set the width of the cropped area
            height: 100, // Set the height of the cropped area
        });

        // Resize the entire output image by reducing its size
        const resizedCanvas = document.createElement('canvas');
        const ctx = resizedCanvas.getContext('2d');
        resizedCanvas.width = 100; // Output width of the final image
        resizedCanvas.height = 100; // Output height of the final image

        // Draw the cropped image onto the resized canvas to shrink it
        ctx.drawImage(croppedCanvas, 0, 0, 500, 500, 0, 0, 300, 300);

        // Display the resized image
        const croppedImageElement = document.getElementById('cropped-image');
        croppedImageElement.src = resizedCanvas.toDataURL(); // Set the source to the resized image
        croppedImageElement.style.display = 'block'; // Show the resized image
    }
}