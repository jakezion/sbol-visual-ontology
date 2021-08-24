/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2010-2013, Silvio Peroni <essepuntato@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SourceExtractor {
    private final List<String> mimeTypes;

    public SourceExtractor() {
        mimeTypes = new ArrayList<String>();
    }

    public void addMimeType(String mimeType) {
        mimeTypes.add(mimeType);
    }

    public void addMimeTypes(String[] mimeTypes) {
        for (String mimeType : mimeTypes) {
            addMimeType(mimeType);
        }
    }

    public void removeMimeType(String mimeType) {
        mimeTypes.remove(mimeType);
    }

    public String exec(File url) throws IOException {
        String result = "";
        String ex = "\n";

        for (String mimeType : mimeTypes) {

            try {
                FileInputStream file = new FileInputStream(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(file));
                String line;

                while ((line = in.readLine()) != null) {
                    result += line + "\n";
                }
                in.close();

                break;
            } catch (Exception e) {
                ex += "# " + e.getMessage() + "\n";
            }
        }

        if (result == null || result.equals("")) {
            throw new IOException("The source can't be downloaded in any permitted format." + ex);
        } else {
            return result;
        }
    }
}
