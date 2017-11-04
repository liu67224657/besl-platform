package com.enjoyf.platform.webapps.common.html.servlet.upload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author <a mailto="yinpengyi@platform.com">Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(100000);
            factory.setRepository(new File("/home/webops/uploadtemp"));

            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();

                    if (item.isFormField()) {

                    } else {

                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }

    }

}
