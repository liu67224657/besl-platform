package com.enjoyf.platform.util.http;

import org.apache.commons.httpclient.methods.multipart.PartBase;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/8/13
 * Description:
 */
public class ByteArrayPart extends PartBase {

    private byte[] mData;
    private String mName;

    public ByteArrayPart(byte[] data, String name, String type)
            throws IOException {
        super(name, type, "UTF-8", "binary");
        mName = name;
        mData = data;
    }

    protected void sendData(OutputStream out) throws IOException {
        out.write(mData);
    }

    protected long lengthOfData() throws IOException {
        return mData.length;
    }

    protected void sendDispositionHeader(OutputStream out)
            throws IOException {
        super.sendDispositionHeader(out);
        StringBuilder buf = new StringBuilder();
        buf.append("; filename=\"").append(mName).append("\"");
        out.write(buf.toString().getBytes());
    }
}
