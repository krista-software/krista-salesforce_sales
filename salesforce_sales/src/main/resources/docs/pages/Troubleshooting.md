# Troubleshooting

## Overview

This guide provides solutions to common issues encountered when using the Salesforce Sales extension. Follow these troubleshooting steps to resolve configuration, authentication, and operational problems.

## Authentication Issues

### Problem: OAuth Authentication Fails

**Symptoms:**
- "Invalid client credentials" error
- "Authorization code expired" error
- Unable to complete OAuth flow

**Solutions:**
1. **Verify Client Credentials**
   - Check Consumer Key and Consumer Secret in extension configuration
   - Ensure credentials match those in your Salesforce Connected App
   - Verify the Connected App is enabled

2. **Check OAuth Scopes**
   - Ensure all required scopes are configured:
     - `api`, `web`, `refresh_token`, `offline_access`
     - `eclair_api`, `wave_api`, `chatter_api`
     - `visualforce`, `custom_permissions`
     - `id`, `profile`, `email`, `address`, `phone`, `openid`, `full`

3. **Validate Callback URL**
   - Ensure callback URL in Connected App matches extension configuration
   - Check for HTTPS requirement in production environments

### Problem: Token Refresh Fails

**Symptoms:**
- "Invalid refresh token" error
- Frequent re-authentication requests
- Session timeout errors

**Solutions:**
1. **Check Token Storage**
   - Verify refresh tokens are being stored securely
   - Ensure token storage mechanism is working correctly

2. **Review Token Policies**
   - Check Salesforce session timeout settings
   - Verify refresh token policies in your org
   - Ensure Connected App has appropriate token policies

## Configuration Issues

### Problem: Extension Not Loading

**Symptoms:**
- Extension appears offline or unavailable
- Configuration errors in Krista platform
- Missing catalog requests

**Solutions:**
1. **Verify Extension Configuration**
   - Check all required configuration parameters are set
   - Validate Salesforce instance URL format
   - Ensure API version is supported (v58.0+)

2. **Check Network Connectivity**
   - Verify network access to Salesforce instance
   - Check firewall and proxy settings
   - Test connectivity to `https://your-instance.salesforce.com`

3. **Review Logs**
   - Check extension logs for specific error messages
   - Look for authentication or connection errors
   - Verify API call responses

### Problem: Catalog Requests Failing

**Symptoms:**
- "Method not found" errors
- Empty or unexpected responses
- Permission denied errors

**Solutions:**
1. **Verify User Permissions**
   - Ensure authenticated user has required object permissions
   - Check field-level security settings
   - Verify profile and permission set assignments

2. **Check API Limits**
   - Monitor daily API call limits
   - Check for rate limiting issues
   - Verify concurrent request limits

## Data Issues

### Problem: Missing or Incomplete Data

**Symptoms:**
- Empty result sets when data should exist
- Missing fields in responses
- Inconsistent data between requests

**Solutions:**
1. **Check Field Permissions**
   - Verify user has read access to all required fields
   - Check field-level security settings
   - Ensure custom fields are accessible

2. **Review Sharing Rules**
   - Check record sharing settings
   - Verify territory and role-based access
   - Ensure proper data visibility

3. **Validate Filters**
   - Check filter criteria in requests
   - Verify date ranges and search parameters
   - Ensure filter values are valid

### Problem: Data Synchronization Issues

**Symptoms:**
- Stale or outdated data
- Changes not reflected immediately
- Inconsistent data across different requests

**Solutions:**
1. **Check Caching Settings**
   - Verify cache expiration policies
   - Clear cache if necessary
   - Review caching configuration

2. **Review Update Frequency**
   - Check how often data is refreshed
   - Verify real-time vs. batch update settings
   - Ensure proper change tracking

## Performance Issues

### Problem: Slow Response Times

**Symptoms:**
- Requests taking longer than expected
- Timeout errors
- Poor user experience

**Solutions:**
1. **Optimize Queries**
   - Use appropriate filters to limit result sets
   - Implement pagination for large datasets
   - Avoid unnecessary field retrievals

2. **Check System Load**
   - Monitor Salesforce org performance
   - Check for concurrent user load
   - Verify system maintenance schedules

3. **Review Network Performance**
   - Test network latency to Salesforce
   - Check bandwidth availability
   - Verify CDN and proxy performance

## Error Codes and Messages

### Common Error Codes

| Error Code | Description | Solution |
|------------|-------------|----------|
| INVALID_LOGIN | Authentication failed | Check credentials and OAuth configuration |
| INSUFFICIENT_ACCESS | Permission denied | Verify user permissions and sharing rules |
| REQUEST_LIMIT_EXCEEDED | API limit reached | Monitor and manage API usage |
| INVALID_FIELD | Field not accessible | Check field permissions and existence |
| MALFORMED_QUERY | Invalid query syntax | Review query structure and parameters |

### HTTP Status Codes

| Status Code | Meaning | Common Causes |
|-------------|---------|---------------|
| 401 | Unauthorized | Invalid or expired authentication |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Invalid endpoint or record ID |
| 429 | Too Many Requests | Rate limiting exceeded |
| 500 | Internal Server Error | Salesforce system issue |

## Getting Help

### Before Contacting Support

1. **Gather Information**
   - Error messages and codes
   - Request/response details
   - Configuration settings
   - Steps to reproduce the issue

2. **Check Documentation**
   - Review relevant catalog request documentation
   - Check entity field definitions
   - Verify configuration requirements

3. **Test in Isolation**
   - Try with minimal configuration
   - Test with different user accounts
   - Isolate the specific failing component

### Support Resources

- **Extension Documentation**: Complete catalog request and entity documentation
- **Salesforce Help**: Official Salesforce documentation and support
- **Community Forums**: User community discussions and solutions
- **Technical Support**: Direct support for complex issues

### Log Collection

When reporting issues, include:
- Extension configuration (sanitized)
- Error logs and stack traces
- Request/response examples
- System environment details
- Steps to reproduce the problem

## Preventive Measures

### Regular Maintenance

1. **Monitor API Usage**
   - Track daily API call consumption
   - Set up alerts for approaching limits
   - Optimize high-volume requests

2. **Review Permissions**
   - Regularly audit user permissions
   - Update sharing rules as needed
   - Maintain proper security settings

3. **Update Configuration**
   - Keep extension configuration current
   - Update API versions when available
   - Review and refresh OAuth tokens

### Best Practices

1. **Error Handling**
   - Implement proper error handling in integrations
   - Use retry logic for transient failures
   - Log errors for troubleshooting

2. **Performance Optimization**
   - Use efficient query patterns
   - Implement appropriate caching
   - Monitor and optimize slow requests

3. **Security**
   - Regularly rotate OAuth credentials
   - Monitor for unauthorized access
   - Keep security settings up to date

## See Also

- [Authentication](pages/Authentication.md)
- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Creating Salesforce App](pages/CreatingSalesforceApp.md)
