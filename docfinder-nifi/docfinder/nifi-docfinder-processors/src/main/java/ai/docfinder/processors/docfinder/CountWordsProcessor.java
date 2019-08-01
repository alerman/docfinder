package ai.docfinder.processors.docfinder;


import com.google.gson.Gson;
import document.DocfinderDocument;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.commons.io.IOUtils;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;

import java.io.File;
import java.io.IOException;

import static ai.docfinder.processors.docfinder.CreateDocfinderDocumentProcessor.FAILURE;


public class CountWordsProcessor extends AbstractProcessor {



    @Override
    public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();

        //TODO this should be done oncreate and not on each flowfile
        try {
            TokenizerModel model = new TokenizerModel(new File("/path/to/model"));
            Tokenizer tokenizer = new TokenizerME(model);

            session.read(flowFile, in -> {
                try {
                    String content = IOUtils.toString(in);
                    DocfinderDocument document = new Gson().fromJson(content, DocfinderDocument.class);
                    String[] tokens = tokenizer.tokenize(document.getDocumentText().getContent());
                    System.out.println(tokens.length);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    getLogger().error("Failed to read json string.");
                    session.transfer(flowFile, FAILURE);
                }
            });
        } catch (IOException e)
        {
            throw new IllegalStateException("Some exception happened trying open tokenizer", e);
        }
    }
}
