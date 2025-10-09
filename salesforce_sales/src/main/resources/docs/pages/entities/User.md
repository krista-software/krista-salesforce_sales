# User Entity

## Overview

The User entity represents individuals who have access to the Salesforce Sales system. Users can be sales representatives, managers, administrators, or other team members who interact with the CRM system.

## Entity Details

- **Entity Name**: User
- **Primary Key**: Id
- **Searchable Fields**: User name, Name, Email
- **Display Fields**: User name, Name, Email

## Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the User record |
| User name | Text | Yes | Login username (searchable) |
| Name | Text | No | Full display name (searchable) |
| First Name | Text | No | User's first name |
| Last Name | Text | Yes | User's last name |
| Email | Text | Yes | Primary email address (searchable) |
| Alias | Text | Yes | Short alias for the user |
| Company Name | Text | No | Name of the user's company |
| Mobile Number | Phone | No | Mobile phone number |
| Community Nickname | Text | No | Nickname for community features |

## Usage in Catalog Requests

Users are referenced as owners and assignees in various catalog requests for accounts, opportunities, leads, tasks, and assets.

## Example Usage

```json
{
  "id": "0055w00000ABC123",
  "userName": "john.smith@company.com",
  "name": "John Smith",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@company.com",
  "alias": "jsmith"
}
```

## See Also

- [Account Entity](pages/entities/Account.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Task Entity](pages/entities/Task.md)
