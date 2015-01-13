function initFileUpload() {
	console.log('initFileUp');
	//var dest = document.getElementById("text");
	//dest.addEventListener("dragover", onDragOver, false);
	//dest.addEventListener("drop", onDrop, false);
}

function onDragOver(ev) {
	console.log('onDragOver');
	ev.stopPropagation();
    ev.preventDefault();
    ev.dataTransfer.dropEffect = 'copy';
}

function onDrop(ev) {
	console.log('onDrop');
	// ev.stopPropagation();
	var dt = ev.dataTransfer;
	var files = dt.files;
	ev.preventDefault();
	if (files.length > 0) {
		var file = files[0];
		var reader = new FileReader();
		reader.onerror = function() {
			alert('Erro na leitura');
		};
		reader.onloadend = function() {
			var content = reader.result;
			sendMessage2('FILE_UPLOAD', 1, [file.name, base64encode(content)].join('|'));
			alert('Upload efetuado com sucesso!');
		};
		reader.readAsBinaryString(file);
	} else {
		alert('Nenhum arquivo');
	}
	return false;
}