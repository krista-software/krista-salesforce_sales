# Send Verification Mail

## Overview

The Send Verification Mail catalog request sends verification emails to confirm email addresses or validate user actions in Salesforce Sales. This request is commonly used for email verification workflows and security confirmations.

## Request Details

- **Catalog Request Name**: Send Verification Mail
- **Description**: Send verification emails for email address confirmation
- **Type**: Communication Action
- **Returns**: Verification email send confirmation

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| emailAddress | Email | Yes | Email address to verify |
| verificationType | PickOne | Yes | Type of verification (EmailVerification, PasswordReset, AccountActivation) |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Text | No | User ID associated with the verification |
| customMessage | Text | No | Custom message to include in verification email |
| expirationHours | Number | No | Hours until verification link expires (default: 24) |
| redirectUrl | Text | No | URL to redirect after successful verification |
| templateId | Text | No | Custom email template ID |

## Verification Types

### Email Verification
- **Purpose**: Confirm email address ownership
- **Use Case**: New user registration, email address changes
- **Expiration**: 24 hours (default)

### Password Reset
- **Purpose**: Secure password reset process
- **Use Case**: Forgotten password recovery
- **Expiration**: 2 hours (recommended)

### Account Activation
- **Purpose**: Activate new user accounts
- **Use Case**: New employee onboarding
- **Expiration**: 48 hours (recommended)

## Request Examples

### Basic Email Verification
```json
{
  "emailAddress": "newuser@company.com",
  "verificationType": "EmailVerification",
  "userId": "0055w00000ABC123",
  "expirationHours": 24
}
```

### Password Reset
```json
{
  "emailAddress": "user@company.com",
  "verificationType": "PasswordReset",
  "userId": "0055w00000ABC123",
  "expirationHours": 2,
  "customMessage": "You requested a password reset for your Salesforce account."
}
```

### Account Activation
```json
{
  "emailAddress": "employee@company.com",
  "verificationType": "AccountActivation",
  "userId": "0055w00000ABC123",
  "expirationHours": 48,
  "redirectUrl": "https://company.salesforce.com/welcome",
  "customMessage": "Welcome to the team! Please activate your account."
}
```

## Response Format

### Successful Send
```json
{
  "success": true,
  "verificationId": "0Vs5w00000ABC123",
  "emailAddress": "user@company.com",
  "verificationType": "EmailVerification",
  "sentDate": 1640995200000,
  "expirationDate": 1641081600000,
  "status": "Sent"
}
```

### Error Response
```json
{
  "success": false,
  "error": "INVALID_EMAIL",
  "message": "The provided email address is not valid",
  "emailAddress": "invalid-email"
}
```

## Verification Process

### Email Verification Flow
1. **Request Sent**: Verification email sent to user
2. **User Clicks Link**: User clicks verification link in email
3. **Token Validated**: System validates verification token
4. **Email Confirmed**: Email address marked as verified
5. **Redirect**: User redirected to specified URL (optional)

### Security Features
- **Unique Tokens**: Each verification uses a unique, secure token
- **Time Expiration**: Links expire after specified time period
- **Single Use**: Verification links can only be used once
- **IP Tracking**: Track IP addresses for security auditing

## Common Use Cases

1. **New User Registration**: Verify email addresses for new users
2. **Email Address Changes**: Confirm new email addresses
3. **Password Recovery**: Secure password reset process
4. **Account Activation**: Activate dormant or new accounts
5. **Security Verification**: Confirm sensitive account changes
6. **Two-Factor Authentication**: Email-based 2FA verification

## Email Templates

### Default Templates
- Standard verification email templates provided
- Professional formatting and branding
- Multiple language support
- Mobile-responsive design

### Custom Templates
- Use custom email templates for branded experience
- Include company logos and styling
- Personalized messaging
- Custom verification workflows

## Best Practices

1. **Clear Instructions**: Provide clear verification instructions
2. **Appropriate Expiration**: Set reasonable expiration times
3. **Security Messaging**: Include security best practices
4. **Mobile Friendly**: Ensure emails work on mobile devices
5. **Fallback Options**: Provide alternative verification methods
6. **Audit Logging**: Log all verification attempts

## Error Handling

### Common Errors
- **INVALID_EMAIL**: Email address format is invalid
- **USER_NOT_FOUND**: Specified user ID doesn't exist
- **TEMPLATE_NOT_FOUND**: Custom template doesn't exist
- **DAILY_LIMIT_EXCEEDED**: Daily email limit reached
- **DUPLICATE_REQUEST**: Verification already pending

### Error Prevention
- Validate email addresses before sending
- Verify user exists and is active
- Check template availability
- Monitor daily email limits
- Implement rate limiting for verification requests

## Security Considerations

### Token Security
- Tokens are cryptographically secure
- Tokens are single-use only
- Tokens expire automatically
- Tokens cannot be guessed or brute-forced

### Audit Trail
- All verification attempts logged
- IP addresses tracked
- Success/failure status recorded
- User actions audited

## Integration Patterns

### User Registration Flow
```json
{
  "emailAddress": "newuser@company.com",
  "verificationType": "EmailVerification",
  "customMessage": "Please verify your email to complete registration",
  "redirectUrl": "https://app.company.com/welcome"
}
```

### Password Reset Flow
```json
{
  "emailAddress": "user@company.com",
  "verificationType": "PasswordReset",
  "expirationHours": 1,
  "customMessage": "Click the link below to reset your password"
}
```

## See Also

- [Send A Mail](pages/SendAMail.md)
- [User Entity](pages/entities/User.md)
- [Authentication](pages/Authentication.md)
