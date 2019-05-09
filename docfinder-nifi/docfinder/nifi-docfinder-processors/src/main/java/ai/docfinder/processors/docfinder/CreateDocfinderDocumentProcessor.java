/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.docfinder.processors.docfinder;

import ai.docfinder.document.DocfinderDocument;
import ai.docfinder.document.DocumentDetail;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.io.OutputStreamCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"example"})
@CapabilityDescription("Provide a description")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class CreateDocfinderDocumentProcessor extends AbstractProcessor {

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Successful creation of DocfinderDocument")
            .build();
    public static final Relationship FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Failure in  creation of DocfinderDocument")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(SUCCESS);
        relationships.add(FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {

    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if ( flowFile == null ) {
            return;
        }

        DocfinderDocument finderDocument = new DocfinderDocument();

        finderDocument.setFileReceivedTime(flowFile.getEntryDate());
        String decompressedFilenameAttribute = "segment.original.filename";
        if(flowFile.getAttributes().containsKey(decompressedFilenameAttribute)) {
            finderDocument.setSourceFileName(flowFile.getAttribute(decompressedFilenameAttribute));
        }
        finderDocument.setOriginalFileName(flowFile.getAttribute("filename"));


        String originalFilePath = flowFile.getAttribute("absolute.path");

        finderDocument.setOriginalFilePath(originalFilePath);

        DocumentDetail finderDetail = new DocumentDetail();
        finderDetail.setTitle(finderDocument.getOriginalFileName());
        finderDocument.setDocumentText(finderDetail);

        session.read(flowFile, in -> {
            try{
                String content = IOUtils.toString(in);
                finderDetail.setContent(content);
            }catch(Exception ex){
                ex.printStackTrace();
                getLogger().error("Failed to read json string.");
                session.transfer(flowFile, FAILURE);
            }
        });

        FlowFile newFlowFile = session.write(flowFile, (OutputStreamCallback) out -> {
            out.write(new Gson().toJson(finderDocument).getBytes());
        });
        session.transfer(newFlowFile, SUCCESS);

    }
}
