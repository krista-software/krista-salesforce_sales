# Account Change Alert

## Overview

The Account Change Alert catalog request monitors changes to account records in Salesforce Sales and sends notifications when specified account fields are modified. This enables real-time tracking of important account updates.

## Request Details

- **Catalog Request Name**: Account Change Alert
- **Description**: Monitor and receive alerts for account record changes
- **Type**: Alert/Notification
- **Trigger**: Account field modifications

## Alert Configuration

### Monitored Fields

The following account fields can be monitored for changes:

| Field Name | Description | Alert Priority |
|------------|-------------|----------------|
| Name | Account name changes | High |
| Type | Account type modifications | Medium |
| Industry | Industry classification changes | Medium |
| Annual Revenue | Revenue updates | High |
| Number of Employees | Employee count changes | Low |
| Owner Id | Account ownership changes | High |
| Phone | Primary phone number changes | Low |
| Website | Website URL changes | Low |
| Billing Address | Billing address modifications | Medium |
| Shipping Address | Shipping address modifications | Medium |

### Alert Triggers

| Trigger Type | Description |
|--------------|-------------|
| Field Change | Any monitored field value changes |
| Record Creation | New account records created |
| Record Deletion | Account records deleted |
| Ownership Change | Account owner assignment changes |

## Parameters

### Alert Configuration Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| accountIds | Text Array | No | Specific account IDs to monitor |
| ownerIds | Text Array | No | Monitor accounts owned by specific users |
| accountTypes | PickOne Array | No | Monitor accounts of specific types |
| industries | PickOne Array | No | Monitor accounts in specific industries |
| fieldsToMonitor | Text Array | No | Specific fields to monitor for changes |

### Notification Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| notificationEmail | Email | No | Email address for alert notifications |
| webhookUrl | Text | No | Webhook URL for real-time notifications |
| alertFrequency | PickOne | No | Frequency of alert batching (Immediate, Hourly, Daily) |

## Alert Payload

When an account change is detected, the alert contains:

```json
{
  "alertType": "AccountChange",
  "timestamp": 1640995200000,
  "accountId": "0015w00000ABC123",
  "accountName": "Acme Corporation",
  "changeType": "FieldUpdate",
  "changedFields": [
    {
      "fieldName": "Annual Revenue",
      "oldValue": 5000000,
      "newValue": 7500000,
      "changeTimestamp": 1640995200000
    }
  ],
  "changedBy": {
    "userId": "0055w00000ABC123",
    "userName": "John Smith"
  }
}
```

## Example Configurations

### Monitor High-Value Accounts
```json
{
  "accountTypes": ["Customer - Direct"],
  "fieldsToMonitor": ["Annual Revenue", "Number of Employees", "Owner Id"],
  "alertFrequency": "Immediate",
  "notificationEmail": "sales-manager@company.com"
}
```

### Track New Customer Acquisitions
```json
{
  "accountTypes": ["Prospect", "Customer - Direct"],
  "fieldsToMonitor": ["Type"],
  "alertFrequency": "Immediate",
  "webhookUrl": "https://api.company.com/webhooks/account-changes"
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

## Common Use Cases

1. **Revenue Tracking**: Monitor changes to annual revenue for forecasting
2. **Territory Management**: Track account ownership changes
3. **Customer Lifecycle**: Monitor account type changes (Prospect → Customer)
4. **Data Quality**: Alert on critical field updates requiring validation
5. **Compliance**: Track changes for audit and compliance purposes
6. **Sales Operations**: Monitor high-value account modifications

## Alert Management

### Alert Frequency Options
- **Immediate**: Real-time alerts for each change
- **Hourly**: Batched alerts sent every hour
- **Daily**: Daily summary of all changes

### Alert Filtering
- Filter by account importance or value
- Set thresholds for revenue changes
- Focus on specific account types or industries
- Monitor only business-critical fields

## Best Practices

1. **Selective Monitoring**: Monitor only critical fields to reduce noise
2. **Appropriate Frequency**: Choose alert frequency based on urgency
3. **Clear Recipients**: Send alerts to relevant stakeholders
4. **Action Plans**: Define response procedures for different alert types
5. **Regular Review**: Periodically review and adjust alert configurations

## Integration Options

### Email Notifications
- Formatted email alerts with change details
- Customizable email templates
- Multiple recipient support

### Webhook Integration
- Real-time HTTP callbacks
- JSON payload with change details
- Integration with external systems

### Dashboard Integration
- Real-time dashboard updates
- Visual change indicators
- Historical change tracking

## See Also

- [Account Entity](pages/entities/Account.md)
- [Lead Change Alert](pages/LeadChangeAlert.md)
- [Task Change Alert](pages/TaskChangeAlert.md)
- [User Entity](pages/entities/User.md)
