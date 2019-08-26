<html>

<head>
	<title>Local  data files from Projecte Collab</title>
</head>


<body>
	<h1>Local data files from Projecte Collab</h1>
	<hr />

<?php

$filename = $_GET['filename'];

if ($filename == '') {
	$files = scandir('data');

	echo ("<h3>Choose your file:</h3>");
	foreach ($files as $f) {
		if (($f !== ".") && ($f !== ".."))
			echo ("<p><a href='index.php?filename=".$f."'>".$f."</a></p>");
	}
} else {

	echo ("<h3>Listing of file ".$filename."</h3>");

	$lines = 0;
	$min_value = 100.0;
	$max_value = -100.0;
	if (($f = fopen("data/".$filename, "r")) !== FALSE) {
                while (($row = fgetcsv($f, 1000, ",")) !== FALSE) {
                        $va = $row[6];
			$vd = floatval($va);

			if ($vd > $max_value) $max_value = $vd;
			if ($vd < $min_value) $min_value = $vd;
			$lines += 1;
		}
		fclose($f);
	}

        if (($f = fopen("data/".$filename, "r")) !== FALSE) {

		$width = 800;
		$height = 400;
		$image = ImageCreate($width, $height);
		$background_color = imagecolorallocate($image, 255, 255, 255);
		$text_color = imagecolorallocate($image, 0, 0, 0);
		$line_color = imagecolorallocate($image, 0, 0, 180);

		imagerectangle($image, 0, 0, $width - 1, $height - 1, $text_color);
		imagestring($image, 4, $width / 2, 10, $filename, $text_color);
		imagestring($image, 2, 10, 10, $max_value, $text_color);
		imagestring($image, 2, 10, $height - 20, $min_value, $text_color);

		$i = 0;
		while (($row = fgetcsv($f, 1000, ",")) !== FALSE) {
			$va = $row[6];

			$x = intval($i * 1.0 / $lines * $width);
			$y = intval(($max_value - floatval($va)) / ($max_value - $min_value) * $height);
			if ($i !== 0)
				imageline($image, $x0, $y0, $x, $y, $line_color);
			$x0 = $x;
			$y0 = $y;
			$i += 1;
		}
		fclose($f);

		ImagePNG($image, 'output.png');
		ImageDestroy($image);
		echo ("<p align=center><img src='output.png'/></p>");
	}

}
?>

</body>
</html>
