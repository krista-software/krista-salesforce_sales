package app.krista.extensions.crm.sales.salesforce_sales.service;

/**
 * Service interface for Salesforce ContentDocumentLink.
 * Provides getter methods for all ContentDocumentLink fields.
 */
public interface ContentDocumentLink {
    
    /**
     * @return Unique identifier for the ContentDocumentLink
     */
    String getId();
    
    /**
     * @return ID of the ContentDocument (file)
     */
    String getContentDocumentId();
    
    /**
     * @return ID of the linked entity (e.g., Order, Account, Opportunity)
     */
    String getLinkedEntityId();
    
    /**
     * @return Share type (V=Viewer, C=Collaborator, I=Inferred)
     */
    String getShareType();
    
    /**
     * @return Visibility (AllUsers, InternalUsers, SharedUsers)
     */
    String getVisibility();
}

