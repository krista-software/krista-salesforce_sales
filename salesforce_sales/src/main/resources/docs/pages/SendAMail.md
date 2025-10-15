# Send A Mail

## Overview

The Send A Mail catalog request sends email messages through Salesforce Sales. This request enables automated email communication with customers, prospects, and team members directly from the CRM system.

## Request Details

- **Area**: Actions
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Email Addresses | Text | Yes | Comma-separated list of recipient email addresses | "customer@example.com, prospect@company.com" |
| Subject | Text | Yes | Email subject line | "Follow-up on Your Inquiry" |
| Body | Paragraph | Yes | Email message body (plain text or HTML) | "Dear Customer, Thank you for your interest..." |

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Status | Switch (Boolean) | Returns `true` if email was sent successfully, `false` if it failed |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Email Addresses Required | "Email address is required. Please provide at least one recipient email address." | Provide at least one valid email address |
| Subject Required | "Email subject is required. Please provide a subject for the email." | Provide a subject line for the email |
| Body Required | "Email body is required. Please provide content for the email message." | Provide message content in the Body field |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid required parameters
**Error Messages**:
- "Email address is required. Please provide at least one recipient email address."
- "Email subject is required. Please provide a subject for the email."
- "Email body is required. Please provide content for the email message."

**Resolution**:
1. Ensure Email Addresses parameter is provided and not empty
2. Ensure Subject parameter is provided and not empty
3. Ensure Body parameter is provided and not empty
4. Verify email addresses are in valid format

### Logic Errors (LOGIC_ERROR)
**Cause**: Invalid email addresses or Salesforce email service errors
**Resolution**:
1. Verify all email addresses are in valid format (e.g., user@domain.com)
2. Check that email addresses are not blocked or bounced
3. Ensure the authenticated user has permission to send emails
4. Verify Salesforce email deliverability settings

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API errors or connection issues
**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Ensure Salesforce API limits haven't been exceeded
4. Check Salesforce email service status

### Authorization Errors
**Cause**: Insufficient permissions to send emails
**Resolution**:
1. Verify the authenticated user has permission to send emails
2. Check Salesforce email deliverability settings
3. Ensure the org's email sending limits haven't been exceeded
4. Contact your Salesforce administrator if permissions are needed

## Usage Examples

### Example 1: Send Basic Email
**Input**:
```
Email Addresses: "customer@example.com"
Subject: "Follow-up on Your Inquiry"
Body: "Dear Customer, Thank you for your interest in our products. We look forward to working with you."
```

**Output**: `true`

**Result**: Email successfully sent to customer@example.com

### Example 2: Send to Multiple Recipients
**Input**:
```
Email Addresses: "customer1@example.com, customer2@example.com, customer3@example.com"
Subject: "Product Update Announcement"
Body: "We are excited to announce our new product features..."
```

**Output**: `true`

**Result**: Email sent to all three recipients

### Example 3: Missing Subject
**Input**:
```
Email Addresses: "customer@example.com"
Subject: (empty)
Body: "This is the email body"
```

**Output**: Error thrown

**Error**: "Email subject is required. Please provide a subject for the email."

## Business Rules

1. **Email Format**: Email addresses must be in valid format (user@domain.com)
2. **Multiple Recipients**: Separate multiple email addresses with commas
3. **Character Limits**: Subject and body have Salesforce-imposed character limits
4. **Deliverability**: Emails are subject to Salesforce email deliverability settings
5. **Daily Limits**: Salesforce orgs have daily email sending limits

## Limitations

1. **No Attachments**: This request does not support file attachments
2. **No CC/BCC**: Only direct recipients (To:) are supported
3. **No HTML Formatting**: While HTML can be included in the body, advanced formatting may not render correctly
4. **No Templates**: Email templates are not supported in this request
5. **Daily Limits**: Subject to Salesforce org's daily email sending limits (typically 5,000 per day)

## Best Practices

### 1. Validate Email Addresses
Ensure all email addresses are valid before sending to avoid delivery failures.

### 2. Use Clear Subject Lines
Write descriptive subject lines that clearly indicate the email's purpose.

### 3. Keep Body Concise
Write clear, concise email messages that are easy to read and understand.

### 4. Handle Errors Gracefully
Implement proper error handling to manage failed email sends and provide user feedback.

### 5. Monitor Sending Limits
Track your org's daily email sending limits to avoid exceeding quotas.

## Common Use Cases

### 1. Follow-up Communications
```
Scenario: Send follow-up email after a sales meeting
Action: Send email with meeting summary and next steps
Result: Customer receives timely follow-up communication
```

### 2. Customer Notifications
```
Scenario: Notify customers of important account changes
Action: Send email with change details and contact information
Result: Customers are informed of important updates
```

### 3. Welcome Messages
```
Scenario: Send welcome email to new customers
Action: Send email with onboarding information and resources
Result: New customers receive helpful getting-started information
```

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Send Verification Mail](pages/SendVerificationMail.md)
