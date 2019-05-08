package ai.docfinder.document;

import java.io.Serializable;
import java.util.Date;

public class DocfinderDocument implements Serializable {
    private String originalFileName;
    private long fileReceivedTime;
    private String originalFileExtension = "";

    // Increment this version when changing this document
    private long documentMetadataVersion = 1L;

    private DocumentDetail documentText;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public long getFileReceivedTime() {
        return fileReceivedTime;
    }

    public void setFileReceivedTime(long fileReceivedTime) {
        this.fileReceivedTime = fileReceivedTime;
    }

    public String getOriginalFileExtension() {
        return originalFileExtension;
    }

    public void setOriginalFileExtension(String originalFileExtension) {
        this.originalFileExtension = originalFileExtension;
    }

    public DocumentDetail getDocumentText() {
        return documentText;
    }

    public void setDocumentText(DocumentDetail documentText) {
        this.documentText = documentText;
    }
}
