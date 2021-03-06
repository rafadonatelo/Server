package org.kyotogtug.vnc.events;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class EventProcessor {

	static private Robot robot;
	static private ScreenCapturer capturer;

	/** get log instance */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(EventProcessor.class);

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		startCapturing();
	}

	public static void startCapturing() {
		if (capturer == null) {
			capturer = new ScreenCapturer(100);
		}
		if (!capturer.isAlive()) {
			capturer.start();
		}
	}

	public void handleEvent(Event event) {
		if (event instanceof FileEvent) {
			// FileEvent ev = (FileEvent)event;
			if (event.getEventType().equals("FILE_DOWNLOAD")) {

			} else if (event.getEventType().equals("FILE_UPLOAD")) {
				byte[] data = Base64.decodeBase64(event.getData());
				FileOutputStream fout = null;
				try {
					fout = new FileOutputStream("");
					fout.write(data);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (event instanceof ImageEvent) {
			// Respond screen image?
		} else if (event instanceof ImageRequestEvent) {
			if (capturer.isAlive()) {
				sendScreenImage();
			}
		} else if (event instanceof KeyPressEvent) {
			KeyPressEvent ev = (KeyPressEvent) event;
			log.debug("keyPress keyCode=" + ev.getKeyCode());
			robot.keyPress(ev.getKeyCode());
		} else if (event instanceof MouseReleaseEvent) {
			MouseReleaseEvent e = (MouseReleaseEvent) event;
			robot.mouseMove(e.getX(), e.getY());
			robot.mouseRelease(toRobotButtonId(e.getButton()));
		} else if (event instanceof MousePressEvent) {
			MousePressEvent e = (MousePressEvent) event;
			robot.mouseMove(e.getX(), e.getY());
			robot.mousePress(toRobotButtonId(e.getButton()));
		} else if (event instanceof MouseMoveEvent) {
			MouseMoveEvent ev = (MouseMoveEvent) event;
			robot.mouseMove(ev.getX(), ev.getY());
		} else if (event instanceof FileUploadEvent) {
			FileUploadEvent ev = (FileUploadEvent) event;
			ev.save(ev.getFileName());
		} else if (event instanceof FileDownloadRequestEvent) {
			fileDownload();
		} else {
			log.error("...");
		}
	}

	private int toRobotButtonId(int button) {
		switch (button) {
		case 0:
			return InputEvent.BUTTON1_MASK;
		case 1:
			return InputEvent.BUTTON2_MASK;
		case 2:
			return InputEvent.BUTTON3_MASK;
		default:

			break;
		}
		return -1;
	}

	private void sendScreenImage() {
		log.debug("Capture start");
		//String base64Image = capturer.getBase64ImageData();
		/*String data = String.format("IMAGE|0|%d|%s", new Date().getTime(),
				base64Image);*/
		log.debug("Capture end");
	}

	private void fileDownload() {

		Thread thread = new Thread() {
			public void run() {
				try {
					JFileChooser fileChooser = new JFileChooser();
					File selectedFile;
					int ret = fileChooser.showOpenDialog(null);
					if (ret == JFileChooser.APPROVE_OPTION) {
						selectedFile = fileChooser.getSelectedFile();

						File file = new File("html" + File.separator
								+ selectedFile.getName());

						try {
							FileUtils.copyFile(selectedFile, file);

							/*String data = String.format(
									"FILE_DOWNLOAD_RESPONSE|0|%d|%s",
									new Date().getTime(), "/html/"
											+ selectedFile.getName());*/
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};

		thread.start();

	}

}
