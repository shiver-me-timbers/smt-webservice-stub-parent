/*
 *    Copyright 2018 Karl Bennett
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package shiver.me.timbers.webservice.stub.api;

import java.util.List;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Collections.singletonList;
import static shiver.me.timbers.webservice.stub.api.MimeTypes.mimeTypes;

/**
 * @author Karl Bennett
 */
public class StubContentType extends SimpleEntry<String, List<String>> {

    public static final String CONTENT_TYPE = "content-type";

    private final MimeTypes mimeTypes;

    public StubContentType(String value) {
        this(singletonList(value));
    }

    public StubContentType(List<String> value) {
        this(mimeTypes(), value);
    }

    public StubContentType(MimeTypes mimeTypes, List<String> values) {
        super(CONTENT_TYPE, values);
        this.mimeTypes = mimeTypes;
    }

    public boolean isXml() {
        return getValue().stream().anyMatch(this::isXml);
    }

    public boolean isJson() {
        return getValue().stream().anyMatch(this::isJson);
    }

    private boolean isXml(String mimeType) {
        return isSubType(mimeType, "xml");
    }

    private boolean isJson(String mimeType) {
        return isSubType(mimeType, "json");
    }

    private boolean isSubType(String mimeType, String subType) {
        return mimeTypes.containsSubType(mimeType, subType);
    }
}
