package com.codi.frame.utils;

import android.os.ParcelFileDescriptor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * 流操作工具类
 * @author Codi
 */
public final class StreamUtil {

	public static final int IO_BUFFER_SIZE = 8 * 1024;
	private static final int END_OF_STREAM = -1;

	private StreamUtil() {

	}

	public static String[] readLines(final InputStream pInputStream) throws IOException {
		return StreamUtil.readLines(new InputStreamReader(pInputStream));
	}

	public static String[] readLines(final Reader pReader) throws IOException {
		final BufferedReader reader = new BufferedReader(pReader);

		final ArrayList<String> lines = new ArrayList<String>();

		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}

		return lines.toArray(new String[lines.size()]);
	}

	public static final String readFullyByChar(final InputStream pInputStream) throws IOException {
		final StringWriter writer = new StringWriter();
		final char[] buf = new char[StreamUtil.IO_BUFFER_SIZE];
		try {
			final Reader reader = new BufferedReader(new InputStreamReader(pInputStream, "UTF-8"));
			int read;
			while ((read = reader.read(buf)) != StreamUtil.END_OF_STREAM) {
				writer.write(buf, 0, read);
			}
		} finally {
			StreamUtil.close(pInputStream);
		}
		return writer.toString();
	}

    /**
     * Reads the contents of a file, preserving line breaks.
     * @return contents of the given file as a String.
     * @throws IOException
     */
    public static String readFullyByLine(final InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String readStr;
            while ((readStr = reader.readLine()) != null) {
                stringBuilder.append(readStr).append('\n');
            }
            return stringBuilder.toString();
        } finally {
            StreamUtil.close(reader);
        }
    }

    /**
     * Write the contents of a String to a stream.
     * @param outputStream Stream to which the contents will be written.
     * @param contents String with the contents to be written.
     * @throws IOException
     */
    public static void writeToStream(OutputStream outputStream, String contents) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        try {
            writer.write(contents);
        } finally {
            StreamUtil.close(writer);
        }
    }

    /**
     * Write a JSON object to a file
     * @param file file to be written
     * @param contents String with the contents to be written
     * @throws IOException when writing failed
     */
    public static void writeToFile(File file, String contents) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        try {
            writer.write(contents);
        } finally {
            StreamUtil.close(writer);
        }
    }

	public static final byte[] streamToBytes(final InputStream pInputStream) throws IOException {
		return StreamUtil.streamToBytes(pInputStream, StreamUtil.END_OF_STREAM);
	}

	public static final byte[] streamToBytes(final InputStream pInputStream, final int pReadLimit) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream((pReadLimit == StreamUtil.END_OF_STREAM) ? StreamUtil.IO_BUFFER_SIZE : pReadLimit);
		StreamUtil.copy(pInputStream, os, pReadLimit);
		return os.toByteArray();
	}

	/**
	 * @see {@link #streamToBytes(java.io.InputStream, int, byte[], int)}
	 */
	public static final void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData) throws IOException {
		StreamUtil.streamToBytes(pInputStream, pByteLimit, pData, 0);
	}

	/**
	 * @param pInputStream the sources of the bytes.
	 * @param pByteLimit the amount of bytes to read.
	 * @param pData the array to place the read bytes in.
	 * @param pOffset the offset within pData.
	 * @throws java.io.IOException
	 */
	public static final void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData, final int pOffset) throws IOException {
		if (pByteLimit > pData.length - pOffset) {
			throw new IOException("pData is not big enough.");
		}

		int pBytesLeftToRead = pByteLimit;
		int readTotal = 0;
		int read;
		while ((read = pInputStream.read(pData, pOffset + readTotal, pBytesLeftToRead)) != StreamUtil.END_OF_STREAM) {
			readTotal += read;
			if (pBytesLeftToRead > read) {
				pBytesLeftToRead -= read;
			} else {
				break;
			}
		}

		if (readTotal != pByteLimit) {
			throw new IOException("ReadLimit: '" + pByteLimit + "', Read: '" + readTotal + "'.");
		}
	}

	public static final void copy(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
		StreamUtil.copy(pInputStream, pOutputStream, StreamUtil.END_OF_STREAM);
	}

	public static final void copy(final InputStream pInputStream, final byte[] pData) throws IOException {
		int dataOffset = 0;
		final byte[] buf = new byte[StreamUtil.IO_BUFFER_SIZE];
		int read;
		while ((read = pInputStream.read(buf)) != StreamUtil.END_OF_STREAM) {
			System.arraycopy(buf, 0, pData, dataOffset, read);
			dataOffset += read;
		}
	}

	public static final void copy(final InputStream pInputStream, final ByteBuffer pByteBuffer) throws IOException {
		final byte[] buf = new byte[StreamUtil.IO_BUFFER_SIZE];
		int read;
		while ((read = pInputStream.read(buf)) != StreamUtil.END_OF_STREAM) {
			pByteBuffer.put(buf, 0, read);
		}
	}

	/**
	 * Copy the content of the input stream into the output stream, using a temporary
	 * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 *
	 * @param pInputStream The input stream to copy from.
	 * @param pOutputStream The output stream to copy to.
	 * @param pByteLimit not more than so much bytes to read, or unlimited if {@link #END_OF_STREAM}.
	 *
	 * @throws java.io.IOException If any error occurs during the copy.
	 */
	public static final void copy(final InputStream pInputStream, final OutputStream pOutputStream, final int pByteLimit) throws IOException {
		if (pByteLimit == StreamUtil.END_OF_STREAM) {
			final byte[] buf = new byte[StreamUtil.IO_BUFFER_SIZE];
			int read;
			while ((read = pInputStream.read(buf)) != StreamUtil.END_OF_STREAM) {
				pOutputStream.write(buf, 0, read);
			}
		} else {
			final byte[] buf = new byte[StreamUtil.IO_BUFFER_SIZE];
			final int bufferReadLimit = Math.min((int) pByteLimit, StreamUtil.IO_BUFFER_SIZE);
			long pBytesLeftToRead = pByteLimit;

			int read;
			while ((read = pInputStream.read(buf, 0, bufferReadLimit)) != StreamUtil.END_OF_STREAM) {
				if (pBytesLeftToRead > read) {
					pOutputStream.write(buf, 0, read);
					pBytesLeftToRead -= read;
				} else {
					pOutputStream.write(buf, 0, (int) pBytesLeftToRead);
					break;
				}
			}
		}
		pOutputStream.flush();
	}

	public static final boolean copyAndClose(final InputStream pInputStream, final OutputStream pOutputStream) {
		try {
			StreamUtil.copy(pInputStream, pOutputStream, StreamUtil.END_OF_STREAM);
			return true;
		} catch (final IOException e) {
			return false;
		} finally {
			StreamUtil.close(pInputStream);
			StreamUtil.close(pOutputStream);
		}
	}

	public static final void close(final Closeable pCloseable) {
		if (pCloseable != null) {
			try {
				pCloseable.close();
			} catch (final IOException e) {
				LoggerUtil.Clog().e("Error closing Closable", e);
			}
		}
	}

	public static final void flushAndCloseStream(final OutputStream pOutputStream) {
		if (pOutputStream != null) {
			try {
				pOutputStream.flush();
			} catch (final IOException e) {
				LoggerUtil.Clog().e("Error flusing OutputStream", e);
			} finally {
				StreamUtil.close(pOutputStream);
			}
		}
	}

	public static final void flushAndCloseWriter(final Writer pWriter) {
		if (pWriter != null) {
			try {
				pWriter.flush();
			} catch (final IOException e) {
				LoggerUtil.Clog().e("Error flusing Writer", e);
			} finally {
				StreamUtil.close(pWriter);
			}
		}
	}

    public static String readFromPFD(ParcelFileDescriptor parcelFileDescriptor) {

        if (parcelFileDescriptor == null) {
            return null;
        }

        FileReader fr = null;
        char[] buffer = new char[StreamUtil.IO_BUFFER_SIZE];

        try {
            StringBuilder strBuilder = new StringBuilder();
            fr = new FileReader(parcelFileDescriptor.getFileDescriptor());
            while (fr.read(buffer) != StreamUtil.END_OF_STREAM) {
                strBuilder.append(buffer);
            }

            return strBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fr);
            StreamUtil.close(parcelFileDescriptor);
        }

        return null;
    }
}
