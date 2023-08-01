package com.softpuzzle.angkor.utility;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class FileUtil {
	public static boolean isFileExtensionAllowed(String fileName, String[] allowedExtensions) {
		String fileExtension = StringUtils.getFilenameExtension(fileName);
		if (StringUtils.hasText(fileExtension)) {
			for (String extension : allowedExtensions) {
				if (fileExtension.equalsIgnoreCase(extension)) {
					return true;
				}
			}
		}
		return false;
	}

	public static MultipartFile generateThumbnailAndUpload(MultipartFile videoFile) {

		MultipartFile multipartFile = videoFile;

		try {
			File inputFile = convertMultipartFileToFile(multipartFile);
			File thumbnailFile = extractThumbnailFromFile(inputFile);
			MultipartFile thumbnailMultipartFile = convertFileToMultipartFile(thumbnailFile);

			inputFile.delete();
			thumbnailFile.delete();
			return thumbnailMultipartFile;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
		return file;
	}

	private static File extractThumbnailFromFile(File inputFile) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(
				"/usr/bin/ffmpeg",
				"-i", inputFile.getAbsolutePath(),
				"-f", "image2",
				"-t", "0.001",
				"-ss", "1",
				inputFile.getAbsolutePath() + "_%2d.jpg"
		);
		try {
			Process process = processBuilder.start();
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new IOException("Failed to extract thumbnail from file: " + inputFile.getAbsolutePath());
			}
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			e.printStackTrace();
		}
		File thumbnailFile = new File(inputFile.getParentFile(), inputFile.getAbsolutePath() + "_01.jpg");
		return thumbnailFile;
	}

	private static MultipartFile convertFileToMultipartFile(File file) throws IOException, InterruptedException {
		Thread.sleep(1500);
		FileInputStream fileInputStream = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), null, fileInputStream);
		fileInputStream.close();
		return multipartFile;
	}

	public static HashMap getFilePixelSize(URL url) throws IOException {

		HashMap map = new HashMap();
		ImageInputStream imageStream = ImageIO.createImageInputStream(url.openStream());
		Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
		if (readers.hasNext()) {
			ImageReader reader = readers.next();
			reader.setInput(imageStream, true);
			Integer width = reader.getWidth(0);
			Integer height = reader.getHeight(0);
			map.put("width", width);
			map.put("height", height);
			reader.dispose();
		}
		imageStream.close();
		return map;
	}

	public static MultipartFile compressImage(MultipartFile imageFile) throws Exception {

		float quality = 0.5f;

		BufferedImage image = ImageIO.read(imageFile.getInputStream());
		BufferedImage rotatedImage = rotateImage(image, getExifOrientation(imageFile));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		if (!writers.hasNext()) {
			throw new IllegalStateException("No writers found");
		}

		ImageWriter writer = writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);

		writer.write(null, new IIOImage(rotatedImage, null, null), param);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		MultipartFile compressedFile = new MockMultipartFile(
				imageFile.getName(),
				imageFile.getOriginalFilename(),
				imageFile.getContentType(),
				inputStream
		);

		return compressedFile;
	}

	private static int getExifOrientation(MultipartFile imageFile) throws IOException {
		try (InputStream is = imageFile.getInputStream()) {
			Metadata metadata = ImageMetadataReader.readMetadata(is);
			ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (exifIFD0Directory != null && exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				try {
					return exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
				} catch (MetadataException e) {
					e.printStackTrace();
				}
			}
		} catch (ImageProcessingException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private static BufferedImage rotateImage(BufferedImage image, int orientation) {
		if (orientation == 6) {
			return rotate90DegreesClockwise(image);
		} else if (orientation == 3) {
			return rotate180Degrees(image);
		} else if (orientation == 8) {
			return rotate90DegreesCounterclockwise(image);
		}
		return image;
	}

	private static BufferedImage rotate90DegreesClockwise(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				rotatedImage.setRGB(height - 1 - j, i, image.getRGB(i, j));
			}
		}
		return rotatedImage;
	}

	private static BufferedImage rotate180Degrees(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				rotatedImage.setRGB(width - 1 - i, height - 1 - j, image.getRGB(i, j));
			}
		}
		return rotatedImage;
	}

	private static BufferedImage rotate90DegreesCounterclockwise(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				rotatedImage.setRGB(j, width - 1 - i, image.getRGB(i, j));
			}
		}
		return rotatedImage;
	}
}
