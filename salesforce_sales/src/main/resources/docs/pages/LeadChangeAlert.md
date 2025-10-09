# Lead Change Alert

## Overview

The Lead Change Alert catalog request monitors changes to lead records in Salesforce Sales and sends notifications when specified lead fields are modified. This enables real-time tracking of lead updates and conversion activities.

## Request Details

- **Catalog Request Name**: Lead Change Alert
- **Description**: Monitor and receive alerts for lead record changes
- **Type**: Alert/Notification
- **Trigger**: Lead field modifications

## Alert Configuration

### Monitored Fields

The following lead fields can be monitored for changes:

| Field Name | Description | Alert Priority |
|------------|-------------|----------------|
| Name | Lead name changes | Medium |
| Company | Company name changes | Medium |
| Status | Lead status changes | High |
| Rating | Lead rating changes (Hot, Warm, Cold) | High |
| Lead Source | Source changes | Medium |
| Owner Id | Lead ownership changes | High |
| Email | Email address changes | Low |
| Phone | Phone number changes | Low |
| Is Converted | Conversion status changes | Critical |
| Converted Date | Conversion date updates | High |

### Alert Triggers

| Trigger Type | Description |
|--------------|-------------|
| Status Change | Lead status progression or regression |
| Conversion | Lead converted to Account/Contact/Opportunity |
| Ownership Change | Lead reassigned to different owner |
| Rating Change | Lead rating updated (priority changes) |
| Contact Info Change | Email or phone updates |

## Parameters

### Alert Configuration Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| leadIds | Text Array | No | Specific lead IDs to monitor |
| ownerIds | Text Array | No | Monitor leads owned by specific users |
| leadSources | PickOne Array | No | Monitor leads from specific sources |
| statuses | PickOne Array | No | Monitor leads with specific statuses |
| ratings | PickOne Array | No | Monitor leads with specific ratings |
| fieldsToMonitor | Text Array | No | Specific fields to monitor for changes |

### Notification Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| notificationEmail | Email | No | Email address for alert notifications |
| webhookUrl | Text | No | Webhook URL for real-time notifications |
| alertFrequency | PickOne | No | Frequency of alert batching (Immediate, Hourly, Daily) |
| includeConversions | Switch | No | Include lead conversion alerts (default: true) |

## Alert Payload

When a lead change is detected, the alert contains:

```json
{
  "alertType": "LeadChange",
  "timestamp": 1640995200000,
  "leadId": "00Q5w00000ABC123",
  "leadName": "John Smith",
  "company": "Tech Solutions Inc",
  "changeType": "StatusChange",
  "changedFields": [
    {
      "fieldName": "Status",
      "oldValue": "Open - Not Contacted",
      "newValue": "Working - Contacted",
      "changeTimestamp": 1640995200000
    }
  ],
  "changedBy": {
    "userId": "0055w00000ABC123",
    "userName": "Sarah Johnson"
  },
  "conversionInfo": {
    "isConverted": false,
    "convertedAccountId": null,
    "convertedContactId": null,
    "convertedOpportunityId": null
  }
}
```

## Example Configurations

### Monitor High-Priority Leads
```json
{
  "ratings": ["Hot"],
  "fieldsToMonitor": ["Status", "Rating", "Owner Id"],
  "alertFrequency": "Immediate",
  "notificationEmail": "sales-manager@company.com",
  "includeConversions": true
}
```

### Track Lead Conversions
```json
{
  "fieldsToMonitor": ["Is Converted", "Converted Date"],
  "alertFrequency": "Immediate",
  "webhookUrl": "https://api.company.com/webhooks/lead-conversions"
}
```

### Monitor Territory Changes
```json
{
  "fieldsToMonitor": ["Owner Id"],
  "alertFrequency": "Hourly",
  "notificationEmail": "territory-manager@company.com"
}
```

### Web Lead Tracking
```json
{
  "leadSources": ["Web"],
  "fieldsToMonitor": ["Status", "Rating"],
  "alertFrequency": "Immediate",
  "notificationEmail": "web-leads@company.com"
}
```

## Common Use Cases

1. **Lead Qualification Tracking**: Monitor status changes during qualification process
2. **Conversion Monitoring**: Track lead-to-customer conversions in real-time
3. **Territory Management**: Monitor lead ownership changes and assignments
4. **Lead Scoring**: Track rating changes for lead prioritization
5. **Source Performance**: Monitor leads from specific sources (web, events, etc.)
6. **Sales Process Compliance**: Ensure leads follow proper qualification stages

## Lead Conversion Alerts

### Conversion Event Details
When a lead is converted, the alert includes:
- **Converted Account**: New or existing account created
- **Converted Contact**: Contact record created from lead
- **Converted Opportunity**: Opportunity created (if applicable)
- **Conversion Date**: When the conversion occurred
- **Converting User**: Who performed the conversion

### Conversion Alert Example
```json
{
  "alertType": "LeadConversion",
  "leadId": "00Q5w00000ABC123",
  "leadName": "John Smith",
  "company": "Tech Solutions Inc",
  "conversionInfo": {
    "isConverted": true,
    "convertedAccountId": "0015w00000ABC123",
    "convertedContactId": "0035w00000ABC123", 
    "convertedOpportunityId": "0065w00000ABC123",
    "convertedDate": 1640995200000,
    "convertedBy": "0055w00000ABC123"
  }
}
```

## Best Practices

1. **Selective Monitoring**: Monitor only critical fields to reduce alert noise
2. **Appropriate Frequency**: Choose alert frequency based on business needs
3. **Clear Recipients**: Send alerts to relevant stakeholders
4. **Conversion Focus**: Pay special attention to conversion alerts
5. **Action Plans**: Define response procedures for different alert types
6. **Regular Review**: Periodically review and adjust alert configurations

## Integration Options

### Email Notifications
- Formatted email alerts with lead details
- Customizable email templates
- Multiple recipient support
- Priority-based formatting

### Webhook Integration
- Real-time HTTP callbacks
- JSON payload with change details
- Integration with CRM systems
- Custom workflow triggers

### Dashboard Integration
- Real-time dashboard updates
- Visual change indicators
- Lead pipeline tracking
- Conversion rate monitoring

## See Also

- [Lead Entity](pages/entities/Lead.md)
- [Get Leads](pages/GetLeads.md)
- [Account Change Alert](pages/AccountChangeAlert.md)
- [Task Change Alert](pages/TaskChangeAlert.md)
- [User Entity](pages/entities/User.md)
