package com.softpuzzle.angkor.utility;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import org.apache.commons.codec.binary.Base64;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;

	public static MultipartFile generateQRCodeToS3(String data) throws IOException {

		QRCodeWriter q = new QRCodeWriter();
		String text = data;

		try {
			text = new String(text.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		BitMatrix bitMatrix = null;

		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.MARGIN, 0);
			//QUIET_ZONE_SIZE
			bitMatrix = q.encode(GlobalObjects.getAapiUrl()+ "qrcode?u=" + text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
			//q.encode(contents, format, width, height, hints)
		} catch (WriterException e) {
			e.printStackTrace();
		}

		// BitMatrix를 BufferedImage로 변환
		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

		// BufferedImage를 ByteArrayOutputStream으로 변환
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		byte[] imageBytes = baos.toByteArray();

		// MultipartFile 생성
		MultipartFile multipartFile = new MockMultipartFile(
				data,
				data + ".png",
				"image/png",
				imageBytes
		);

		return multipartFile;
	}

	/*public static String generateQRCode(String data) throws IOException {

		QRCodeWriter q = new QRCodeWriter();
		String text = data;

		try {
			text = new String(text.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		BitMatrix bitMatrix = null;

		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.MARGIN, 0);
			//QUIET_ZONE_SIZE
			bitMatrix = q.encode(GlobalObjects.getAapiUrl()+ "qrcode?u=" + text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
			//q.encode(contents, format, width, height, hints)
		} catch (WriterException e) {
			e.printStackTrace();
		}

		String path = GlobalObjects.getUploadPath() + "qrcode/";

		String fullPath = GlobalObjects.getUploadPath()+"qrcode/"+data+".png";
		File file = new File(fullPath);

		Path directoryPath = Paths.get(path);
		if (!Files.exists(directoryPath)) {
			Files.createDirectories(directoryPath);
		}

		try {
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		URI dataUri = null;

		File file2 = new File( fullPath );

		if( file2.exists() ) {

			URL url = null;

			try {
				url = new File(fullPath).toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			dataUri = getDataURIForURL(url);
		}
		return dataUri.toString();
	}

	public static URI getDataURIForURL(URL url) {
		URI dataUri = null;
		if (null != url) {
			try (InputStream inStreamGuess = url.openStream();
				 InputStream inStreamConvert = url.openStream();
				 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				String contentType = URLConnection.guessContentTypeFromStream(inStreamGuess);
				if (null != contentType) {
					byte[] chunk = new byte[4096];
					int bytesRead;
					while ((bytesRead = inStreamConvert.read(chunk)) > 0) {
						os.write(chunk, 0, bytesRead);
					}
					os.flush();

					String encordText = new String(Base64.encodeBase64(os.toByteArray()));
					dataUri = new URI("data:" + contentType + ";base64," + encordText);

				} else {

				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return dataUri;
	}

	*/
}