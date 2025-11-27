# Salesforce Sales Extension - Architecture Documentation

**Version:** 2.0.15
**Java Version:** Java 21
**Salesforce API:** REST API v58.0+
**Last Updated:** 2025-11-27

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [5-Layer Architecture](#5-layer-architecture)
3. [Component Diagrams](#component-diagrams)
4. [Data Flow](#data-flow)
5. [Authentication Flow](#authentication-flow)
6. [Error Handling Architecture](#error-handling-architecture)
7. [Performance Characteristics](#performance-characteristics)
8. [Scalability and Limitations](#scalability-and-limitations)
9. [Security Architecture](#security-architecture)
10. [Deployment Architecture](#deployment-architecture)

---

## Architecture Overview

The Salesforce Sales Extension follows a **5-layer architecture** pattern that provides clear separation of concerns, testability, and maintainability. The extension integrates Krista with Salesforce Sales Cloud using OAuth 2.0 authentication and REST API v58.0+.

### Key Architectural Principles

- **Separation of Concerns**: Each layer has a distinct responsibility
- **Dependency Injection**: All components use constructor injection for testability
- **Interface-Based Design**: Service layer uses interfaces for abstraction
- **Immutability**: Preference for immutable data structures
- **Fail-Safe**: Graceful error handling with user-friendly messages
- **Stateless**: No session state maintained in extension components

### Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 21 |
| Framework | Krista Extension SDK |
| Dependency Injection | JSR-330 (javax.inject) |
| HTTP Client | Apache HttpClient |
| JSON Processing | Jackson |
| OAuth 2.0 | ScribeJava |
| Logging | SLF4J + Logback |
| Build Tool | Gradle |

---

## 5-Layer Architecture

The extension is organized into 5 distinct layers, each with specific responsibilities:

```
┌─────────────────────────────────────────────────────────────┐
│                    Layer 1: Extension Entry Point            │
│  SalesforceSalesExtension, SalesforceSalesAttributes        │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│              Layer 2: Catalog Request Areas                  │
│  OpportunityManagementArea, LeadsManagementArea, etc.       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│           Layer 3: Service Interfaces (DAO Layer)            │
│  OpportunityDao, LeadDao, QuoteDao, SOQLDao, etc.           │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│    Layer 4: Implementation & Integration                     │
│  SalesforceManager, SalesforceTokenProvider, *DaoImpl       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│              Layer 5: Model & Entity Layer                   │
│  SF* Models, Catalog Entities, Converters                   │
└─────────────────────────────────────────────────────────────┘
```

### Layer 1: Extension Entry Point

**Package:** `app.krista.extensions.crm.sales.salesforce_sales`

**Components:**
- `SalesforceSalesExtension` - Main extension class
- `SalesforceSalesAttributes` - Configuration holder
- `SalesforceSalesRequestAuthenticator` - Request authentication handler

**Responsibilities:**
- Extension lifecycle management (initialization, validation, updates)
- Configuration attribute management and validation
- Request authenticator provisioning
- Custom tabs registration
- Connection testing

**Key Annotations:**
- `@Extension` - Marks the extension entry point
- `@Field.*` - Defines configuration attributes
- `@InvokerRequest` - Handles extension lifecycle events

**Configuration Attributes:**
| Attribute | Type | Required | Description |
|-----------|------|----------|-------------|
| clientId | Text | Yes | OAuth 2.0 Client ID |
| clientSecret | Text (Secured) | Yes | OAuth 2.0 Client Secret |
| Instance URL | Text | No | Salesforce instance URL |
| username | Text | No | Service account username |
| password | Text (Secured) | No | Service account password |
| securityToken | Text (Secured) | No | Security token |
| sandbox | Boolean | No | Use sandbox environment |
| timezone | Text | No | Timezone for operations |

### Layer 2: Catalog Request Areas

**Package:** `app.krista.extensions.crm.sales.salesforce_sales.catalog`

**Functional Areas (11 total):**

1. **OpportunityManagementArea** - 7 catalog requests
   - Get Opportunities, Update Opportunity Status, Update An Opportunity
   - Get My Details, Opportunity Change Alert, Get Latest Posts

2. **LeadsManagementArea** - 2 catalog requests
   - Get Leads, Lead Change Alert

3. **QuoteManagementArea** - 6 catalog requests
   - Get All Quote Status, Update Quote, Add Product To Quote
   - Get Quote Details, Get Quote Line Items

4. **AssetsManagementArea** - 2 catalog requests
   - Delete An Asset, Update An Asset

5. **TaskManagementArea** - 2 catalog requests
   - Get Task Statuses, Task Change Alert

6. **AccountsManagementArea** - 1 catalog request
   - Account Change Alert

7. **ActionsArea** - 2 catalog requests
   - Send A Mail, Send Verification Mail

8. **ReportingArea** - 5 catalog requests
   - Process Query Request, Process Query Request Async
   - Get Results, YTD Sales Revenue Closed, Sales Quota

9. **ForecastingArea** - 1 catalog request
   - Get Sales Forecast For A Specific Quarter

10. **WeeklySummaryArea** - 2 catalog requests
    - Get Revenue Closed, Get Revenue Expected To Close

11. **ContentArea** - 1 catalog request
    - Retrieve Custom Fields For Given Object

**Responsibilities:**
- Expose catalog requests to Krista
- Input parameter validation
- Business logic orchestration
- Error handling and user-friendly messages
- Coordinate between multiple DAOs

**Key Annotations:**
- `@Domain` - Defines the functional domain
- `@CatalogRequest` - Marks catalog request methods
- `@Field.*` - Defines input/output parameters

### Layer 3: Service Interfaces (DAO Layer)

**Package:** `app.krista.extensions.crm.sales.salesforce_sales.service`

**Service Interfaces:**
- `OpportunityDao` - Opportunity operations
- `LeadDao` - Lead operations
- `QuoteDao` - Quote operations
- `AssetDao` - Asset operations
- `TaskDao` - Task operations
- `AccountDao` - Account operations
- `UserDao` - User operations
- `EventDao` - Event operations
- `PriceBookDao` - Pricebook operations
- `PriceBookEntryDao` - Pricebook entry operations
- `SOQLDao` - Generic SOQL query execution
- `SalesPipelineSummaryDao` - Pipeline summary operations

**Responsibilities:**
- Define contracts for data access operations
- Provide abstraction over Salesforce API
- Enable testability through interface-based design
- Support dependency injection

**Design Pattern:** Repository Pattern

### Layer 4: Implementation & Integration

**Package:** `app.krista.extensions.crm.sales.salesforce_sales.impl`

**Core Components:**

#### SalesforceManager
Central component for Salesforce API interactions.

**Responsibilities:**
- HTTP request execution to Salesforce REST API
- Request signing with OAuth tokens
- Response parsing and error handling
- CRUD operations (create, read, update, delete)
- SOQL query execution
- Email sending

**Key Methods:**
- `executeRequest(HttpRequest, SalesforceTokenProvider)` - Execute HTTP request
- `create(String objectType, Map<String, Object> fields)` - Create record
- `update(String objectType, String recordId, Map<String, Object> fields)` - Update record
- `delete(String objectType, String recordId)` - Delete record
- `query(String soqlQuery)` - Execute SOQL query
- `sendEmail(String[] toAddresses, String subject, String body)` - Send email

#### SalesforceTokenProvider
Manages OAuth 2.0 token lifecycle.

**Responsibilities:**
- OAuth 2.0 token acquisition (Authorization Code and Username-Password flows)
- Token refresh mechanism
- Refresh token storage and retrieval
- User vs admin authentication handling
- MustAuthorizeException triggering for re-authentication

**Key Methods:**
- `getSalesforceTokenForUser()` - Get token for user
- `getSalesforceTokenForAdmin()` - Get token for admin
- `refreshToken(String refreshToken)` - Refresh expired token
- `storeRefreshToken(String userId, String refreshToken)` - Store refresh token

#### DAO Implementations
Concrete implementations of service interfaces.

**Pattern:** Each DAO implementation:
1. Injects SalesforceManager
2. Constructs SOQL queries
3. Executes queries via SalesforceManager
4. Transforms SF models to catalog entities
5. Handles errors gracefully

**Examples:**
- `OpportunityDaoImpl` - Opportunity CRUD, revenue calculations
- `LeadDaoImpl` - Lead retrieval and filtering
- `QuoteDaoImpl` - Quote management, line items
- `SOQLDaoImpl` - Generic query execution

### Layer 5: Model & Entity Layer

**Package:** `app.krista.extensions.crm.sales.salesforce_sales.model`
**Package:** `app.krista.extensions.crm.sales.salesforce_sales.catalog.entities`

**Components:**

#### SF* Models (Salesforce API Response Models)
- `SFOpportunity`, `SFLead`, `SFQuote`, `SFAsset`, `SFTask`, `SFAccount`, `SFUser`, `SFEvent`, `SFPricebookEntry`
- Direct mapping to Salesforce API JSON responses
- Used for deserialization

#### Catalog Entities
- `Opportunity`, `Lead`, `Quote`, `Asset`, `Task`, `Account`, `User`, `Event`, `PricebookEntry`, `LatestPosts`, `SalesPipelineSummary`
- Krista catalog type definitions
- Annotated with `@Entity`
- Primary key: "Id"
- supportStore: false

#### Catalog Type Converters
- `OpportunityCatalogType`, `LeadCatalogType`, `QuoteCatalogType`, etc.
- Transform SF models to catalog entities
- Handle data type conversions (dates, numbers, booleans)

#### Service Implementations
- `OpportunityImpl`, `LeadImpl`, `QuoteImpl`, etc.
- Wrap SF models with business logic
- Implement service interfaces

**Transformation Flow:**
```
Salesforce API JSON → SF* Model → Service Implementation → Catalog Entity
```

---

## Component Diagrams

### High-Level Component Diagram

```mermaid
graph TB
    subgraph "Krista Platform"
        KP[Krista Platform]
    end

    subgraph "Extension Layer 1"
        EXT[SalesforceSalesExtension]
        ATTR[SalesforceSalesAttributes]
        AUTH[RequestAuthenticator]
    end

    subgraph "Extension Layer 2 - Catalog Areas"
        OPP[OpportunityManagementArea]
        LEAD[LeadsManagementArea]
        QUOTE[QuoteManagementArea]
        ASSET[AssetsManagementArea]
        TASK[TaskManagementArea]
        ACC[AccountsManagementArea]
        ACT[ActionsArea]
        REP[ReportingArea]
        FORE[ForecastingArea]
        WEEK[WeeklySummaryArea]
        CONT[ContentArea]
    end

    subgraph "Extension Layer 3 - Service Layer"
        OPPDAO[OpportunityDao]
        LEADDAO[LeadDao]
        QUOTEDAO[QuoteDao]
        ASSETDAO[AssetDao]
        TASKDAO[TaskDao]
        ACCDAO[AccountDao]
        USERDAO[UserDao]
        SOQLDAO[SOQLDao]
    end

    subgraph "Extension Layer 4 - Implementation"
        MGR[SalesforceManager]
        TOKEN[SalesforceTokenProvider]
        OPPDAOIMPL[OpportunityDaoImpl]
        LEADDAOIMPL[LeadDaoImpl]
        QUOTEDAOIMPL[QuoteDaoImpl]
    end

    subgraph "Extension Layer 5 - Models"
        SFMODEL[SF* Models]
        ENTITY[Catalog Entities]
        CONVERTER[Type Converters]
    end

    subgraph "External Systems"
        SF[Salesforce API]
        OAUTH[OAuth 2.0 Server]
    end

    KP --> EXT
    EXT --> ATTR
    EXT --> AUTH
    EXT --> OPP
    EXT --> LEAD
    EXT --> QUOTE

    OPP --> OPPDAO
    LEAD --> LEADDAO
    QUOTE --> QUOTEDAO
    ASSET --> ASSETDAO
    TASK --> TASKDAO
    ACC --> ACCDAO
    ACT --> SOQLDAO
    REP --> SOQLDAO

    OPPDAO --> OPPDAOIMPL
    LEADDAO --> LEADDAOIMPL
    QUOTEDAO --> QUOTEDAOIMPL

    OPPDAOIMPL --> MGR
    LEADDAOIMPL --> MGR
    QUOTEDAOIMPL --> MGR

    MGR --> TOKEN
    MGR --> SF
    TOKEN --> OAUTH

    MGR --> SFMODEL
    SFMODEL --> CONVERTER
    CONVERTER --> ENTITY

    style EXT fill:#e1f5ff
    style MGR fill:#ffe1e1
    style TOKEN fill:#ffe1e1
    style SF fill:#e1ffe1
```

---

## Data Flow

### Request Processing Flow

```mermaid
sequenceDiagram
    participant User
    participant Krista
    participant CatalogArea
    participant DAO
    participant Manager
    participant TokenProvider
    participant Salesforce

    User->>Krista: Invoke Catalog Request
    Krista->>CatalogArea: Execute Request

    CatalogArea->>CatalogArea: Validate Input Parameters

    alt Validation Fails
        CatalogArea-->>Krista: IllegalArgumentException
        Krista-->>User: User-Friendly Error Message
    end

    CatalogArea->>DAO: Call DAO Method
    DAO->>DAO: Build SOQL Query
    DAO->>Manager: Execute Query/Operation

    Manager->>TokenProvider: Get Access Token

    alt Token Expired
        TokenProvider->>Salesforce: Refresh Token
        Salesforce-->>TokenProvider: New Access Token
    end

    alt Token Refresh Fails
        TokenProvider-->>Manager: MustAuthorizeException
        Manager-->>DAO: MustAuthorizeException
        DAO-->>CatalogArea: MustAuthorizeException
        CatalogArea-->>Krista: MustAuthorizeException
        Krista-->>User: Re-authentication Required
    end

    TokenProvider-->>Manager: Valid Access Token

    Manager->>Salesforce: HTTP Request (with token)
    Salesforce-->>Manager: HTTP Response

    alt Salesforce Error
        Manager->>Manager: Extract Error Message
        Manager-->>DAO: Exception with Error
        DAO-->>CatalogArea: Exception
        CatalogArea->>CatalogArea: Log Error
        CatalogArea-->>Krista: Empty Result or Exception
    end

    Manager->>Manager: Parse JSON Response
    Manager->>Manager: Transform to SF Model
    Manager-->>DAO: SF Model

    DAO->>DAO: Convert to Catalog Entity
    DAO-->>CatalogArea: Catalog Entity

    CatalogArea-->>Krista: Result
    Krista-->>User: Response
```

### Query Execution Flow

```mermaid
flowchart TD
    A[Catalog Request] --> B{Query Type?}

    B -->|Simple Query| C[Build SOQL Query]
    B -->|Complex Query| D[Use Query Builder]
    B -->|Custom Query| E[User-Provided SOQL]

    C --> F[SOQLDaoImpl]
    D --> F
    E --> F

    F --> G[SalesforceManager.query]

    G --> H{Token Valid?}
    H -->|No| I[Refresh Token]
    I --> J{Refresh Success?}
    J -->|No| K[MustAuthorizeException]
    J -->|Yes| L[Execute HTTP Request]
    H -->|Yes| L

    L --> M[Salesforce REST API]
    M --> N{Response Status?}

    N -->|200 OK| O[Parse JSON Response]
    N -->|400 Bad Request| P[Extract Error Message]
    N -->|401 Unauthorized| Q[Token Expired - Retry]
    N -->|403 Forbidden| R[Permission Error]
    N -->|404 Not Found| S[Record Not Found]
    N -->|500 Server Error| T[Salesforce Error]

    O --> U[Transform to SF Model]
    U --> V[Convert to Catalog Entity]
    V --> W[Return Result]

    P --> X[Log Error]
    R --> X
    S --> X
    T --> X
    X --> Y[Return Empty or Exception]

    Q --> I
    K --> Z[Trigger Re-authentication]
```

### CRUD Operation Flow

```mermaid
flowchart LR
    A[Catalog Request] --> B{Operation Type}

    B -->|CREATE| C[Prepare Record Data]
    B -->|READ| D[Build Query]
    B -->|UPDATE| E[Prepare Update Data]
    B -->|DELETE| F[Get Record ID]

    C --> G[SalesforceManager.create]
    D --> H[SalesforceManager.query]
    E --> I[SalesforceManager.update]
    F --> J[SalesforceManager.delete]

    G --> K[POST /sobjects/ObjectType]
    H --> L[GET /query?q=SOQL]
    I --> M[PATCH /sobjects/ObjectType/Id]
    J --> N[DELETE /sobjects/ObjectType/Id]

    K --> O{Success?}
    L --> O
    M --> O
    N --> O

    O -->|Yes| P[Return Result]
    O -->|No| Q[Handle Error]

    Q --> R[Log Error]
    R --> S[Return Exception or Empty]
```

---


## Authentication Flow

### OAuth 2.0 Authorization Code Flow (User Authentication)

```mermaid
sequenceDiagram
    participant User
    participant Krista
    participant Extension
    participant TokenProvider
    participant Browser
    participant Salesforce
    participant TokenStore

    User->>Krista: Invoke Catalog Request
    Krista->>Extension: Execute Request
    Extension->>TokenProvider: Get Token for User

    TokenProvider->>TokenStore: Check for Refresh Token

    alt No Refresh Token
        TokenProvider-->>Extension: MustAuthorizeException
        Extension-->>Krista: MustAuthorizeException
        Krista->>Browser: Redirect to Salesforce Login
        Browser->>Salesforce: User Login
        Salesforce->>Browser: Authorization Code
        Browser->>Extension: Authorization Code
        Extension->>TokenProvider: Exchange Code for Token
        TokenProvider->>Salesforce: POST /oauth2/token
        Salesforce-->>TokenProvider: Access Token + Refresh Token
        TokenProvider->>TokenStore: Store Refresh Token
        TokenProvider-->>Extension: Access Token
    else Refresh Token Exists
        TokenProvider->>Salesforce: Refresh Token Request

        alt Refresh Success
            Salesforce-->>TokenProvider: New Access Token
            TokenProvider-->>Extension: Access Token
        else Refresh Failed
            TokenProvider-->>Extension: MustAuthorizeException
            Extension-->>Krista: Re-authentication Required
        end
    end

    Extension->>Salesforce: API Request with Token
    Salesforce-->>Extension: Response
    Extension-->>Krista: Result
    Krista-->>User: Response
```

### Username-Password Flow (Service Account Authentication)

```mermaid
sequenceDiagram
    participant Extension
    participant TokenProvider
    participant Salesforce

    Extension->>TokenProvider: Get Token for Admin
    TokenProvider->>TokenProvider: Check Configuration

    alt Username/Password Configured
        TokenProvider->>Salesforce: POST /oauth2/token
        Note over TokenProvider,Salesforce: grant_type=password<br/>username=user@example.com<br/>password=password+securityToken<br/>client_id=...<br/>client_secret=...

        alt Authentication Success
            Salesforce-->>TokenProvider: Access Token
            TokenProvider-->>Extension: Access Token
        else Authentication Failed
            Salesforce-->>TokenProvider: Error Response
            TokenProvider->>TokenProvider: Extract Error Message
            TokenProvider-->>Extension: IllegalArgumentException
        end
    else No Username/Password
        TokenProvider-->>Extension: MustAuthorizeException
    end
```

### Token Refresh Flow

```mermaid
flowchart TD
    A[API Request] --> B{Token Valid?}

    B -->|Yes| C[Use Existing Token]
    B -->|No| D{Refresh Token Available?}

    D -->|Yes| E[Call Refresh Token API]
    D -->|No| F[MustAuthorizeException]

    E --> G{Refresh Success?}

    G -->|Yes| H[Store New Access Token]
    G -->|No| I{Error Type?}

    I -->|Invalid Refresh Token| F
    I -->|Network Error| J[Retry with Backoff]
    I -->|Other Error| K[Log Error]

    H --> C
    C --> L[Execute API Request]

    F --> M[Trigger Re-authentication]
    J --> E
    K --> F
```

### Authentication State Diagram

```mermaid
stateDiagram-v2
    [*] --> Unauthenticated

    Unauthenticated --> Authenticating: User Initiates Request
    Authenticating --> Authenticated: Token Obtained
    Authenticating --> Unauthenticated: Authentication Failed

    Authenticated --> TokenValid: Check Token
    TokenValid --> MakingRequest: Token Valid
    TokenValid --> RefreshingToken: Token Expired

    RefreshingToken --> Authenticated: Refresh Success
    RefreshingToken --> Unauthenticated: Refresh Failed

    MakingRequest --> Authenticated: Request Success
    MakingRequest --> RefreshingToken: 401 Unauthorized
    MakingRequest --> Unauthenticated: Auth Error

    Authenticated --> [*]: Logout/Revoke
```

---

## Error Handling Architecture

### Error Categories and Handling Strategy

```mermaid
graph TB
    A[Error Occurs] --> B{Error Category}

    B -->|INPUT_ERROR| C[Validation Failure]
    B -->|LOGIC_ERROR| D[Business Logic Error]
    B -->|SYSTEM_ERROR| E[Salesforce API Error]
    B -->|AUTH_ERROR| F[Authentication Error]

    C --> C1[IllegalArgumentException]
    C1 --> C2[User-Friendly Message]
    C2 --> C3[Return to User]

    D --> D1[Log Error]
    D1 --> D2[Return Empty Result]
    D2 --> D3[Continue Processing]

    E --> E1{HTTP Status Code}
    E1 -->|400| E2[Bad Request - Log & Return Empty]
    E1 -->|401| E3[Unauthorized - Refresh Token]
    E1 -->|403| E4[Forbidden - Permission Error]
    E1 -->|404| E5[Not Found - Log & Return Empty]
    E1 -->|500| E6[Server Error - Log & Return Empty]

    F --> F1[MustAuthorizeException]
    F1 --> F2[Trigger Re-authentication]
    F2 --> F3[User Re-authenticates]

    E3 --> G{Refresh Success?}
    G -->|Yes| H[Retry Request]
    G -->|No| F1
```

### Error Propagation Flow

```mermaid
sequenceDiagram
    participant Salesforce
    participant Manager
    participant DAO
    participant CatalogArea
    participant Krista
    participant User

    Salesforce-->>Manager: Error Response (400/401/403/404/500)
    Manager->>Manager: Parse Error Response
    Manager->>Manager: Extract Error Message

    alt Authentication Error (401)
        Manager->>Manager: Attempt Token Refresh
        alt Refresh Success
            Manager->>Salesforce: Retry Request
        else Refresh Failed
            Manager-->>DAO: MustAuthorizeException
            DAO-->>CatalogArea: MustAuthorizeException
            CatalogArea-->>Krista: MustAuthorizeException
            Krista-->>User: Re-authentication Required
        end
    else Other Errors
        Manager-->>DAO: Exception with Error Message
        DAO->>DAO: Log Error
        DAO->>DAO: Handle Gracefully
        DAO-->>CatalogArea: Empty Result or Exception
        CatalogArea->>CatalogArea: Log Error
        CatalogArea-->>Krista: Empty Result or User-Friendly Error
        Krista-->>User: Error Message
    end
```

### Common Error Scenarios

| Error Type | HTTP Status | Cause | Handling Strategy | User Impact |
|------------|-------------|-------|-------------------|-------------|
| Invalid Token | 401 | Access token expired | Automatic token refresh | Transparent (retry) |
| Refresh Token Expired | 401 | Refresh token expired | MustAuthorizeException | Re-authentication required |
| Insufficient Permissions | 403 | User lacks permissions | Log error, return empty | Error message displayed |
| Record Not Found | 404 | Invalid record ID | Log error, return empty | Empty result |
| Invalid Request | 400 | Malformed request/query | Log error, return empty | Error message displayed |
| Salesforce Server Error | 500 | Salesforce internal error | Log error, return empty | Error message displayed |
| Rate Limit Exceeded | 429 | Too many API requests | Log error, return empty | Error message displayed |
| Network Timeout | Timeout | Network connectivity | Log error, return empty | Error message displayed |
| Validation Error | N/A | Invalid input parameters | IllegalArgumentException | User-friendly validation message |
| SOQL Error | 400 | Invalid SOQL syntax | Log error, return empty | Error message displayed |

### Error Handling Best Practices

1. **Graceful Degradation**: Return empty results instead of crashing
2. **User-Friendly Messages**: Convert technical errors to actionable messages
3. **Comprehensive Logging**: Log all errors with context for debugging
4. **Automatic Retry**: Retry transient errors (token refresh, network)
5. **Fail-Fast Validation**: Validate inputs before making API calls
6. **Error Context**: Include relevant context (request ID, user ID, operation)

---
## Performance Characteristics

### API Request Performance

| Operation Type | Typical Latency | Max Latency | Throughput | Notes |
|----------------|-----------------|-------------|------------|-------|
| Simple Query (< 100 records) | 200-500ms | 2s | 100 req/min | Depends on network and Salesforce load |
| Complex Query (> 1000 records) | 1-5s | 120s | 20 req/min | Use pagination or async queries |
| CRUD Operations | 300-800ms | 3s | 80 req/min | Single record operations |
| Bulk Operations (200 records) | 2-10s | 30s | 10 req/min | Use Salesforce Bulk API for > 200 |
| Token Refresh | 500-1000ms | 5s | N/A | Cached for 1-2 hours |
| Email Sending | 500-1500ms | 10s | 50 req/min | Depends on email size |

### Performance Optimization Strategies

```mermaid
graph LR
    A[Performance Optimization] --> B[Query Optimization]
    A --> C[Caching Strategy]
    A --> D[Batch Processing]
    A --> E[Connection Pooling]

    B --> B1[Use Selective Filters]
    B --> B2[Limit Result Sets]
    B --> B3[Use Indexed Fields]
    B --> B4[Avoid Formula Fields]

    C --> C1[Cache Reference Data]
    C --> C2[Cache Tokens]
    C --> C3[Cache Metadata]

    D --> D1[Bulk API for Large Ops]
    D --> D2[Batch CRUD Operations]
    D --> D3[Async Query Processing]

    E --> E1[Reuse HTTP Connections]
    E --> E2[Connection Timeout: 30s]
    E --> E3[Socket Timeout: 60s]
```

### Query Performance Guidelines

**Indexed Fields (Fast):**
- Id
- Name
- OwnerId
- CreatedDate
- LastModifiedDate
- SystemModstamp
- RecordTypeId
- Custom fields marked as External ID

**Non-Indexed Fields (Slow):**
- Formula fields
- Long text fields
- Rich text fields
- Non-indexed custom fields

**Query Optimization Examples:**

```sql
-- ❌ Slow: Non-selective query
SELECT Id, Name FROM Opportunity

-- ✅ Fast: Selective query with indexed field
SELECT Id, Name FROM Opportunity WHERE OwnerId = '005...' AND CloseDate = THIS_QUARTER

-- ❌ Slow: Query on formula field
SELECT Id, Name FROM Opportunity WHERE Formula_Field__c = 'Value'

-- ✅ Fast: Query on indexed custom field
SELECT Id, Name FROM Opportunity WHERE External_ID__c = 'EXT-12345'

-- ❌ Slow: Large result set
SELECT Id, Name, Description FROM Opportunity WHERE Amount > 0

-- ✅ Fast: Limited result set with pagination
SELECT Id, Name FROM Opportunity WHERE Amount > 10000 LIMIT 100 OFFSET 0
```

### Memory Consumption

| Component | Typical Memory | Peak Memory | Notes |
|-----------|----------------|-------------|-------|
| Extension Instance | 50-100 MB | 200 MB | Per extension instance |
| Query Result (100 records) | 1-5 MB | 10 MB | Depends on field count |
| Query Result (1000 records) | 10-50 MB | 100 MB | Consider pagination |
| Token Cache | < 1 MB | 5 MB | Per user token |
| HTTP Connection Pool | 5-10 MB | 20 MB | Reused connections |

### Concurrency Characteristics

```mermaid
graph TD
    A[Concurrent Requests] --> B{Request Type}

    B -->|Read Operations| C[High Concurrency]
    B -->|Write Operations| D[Medium Concurrency]
    B -->|Token Refresh| E[Serialized]

    C --> C1[Up to 25 concurrent]
    C1 --> C2[Salesforce Limit]

    D --> D1[Up to 10 concurrent]
    D1 --> D2[Avoid Lock Contention]

    E --> E1[1 at a time per user]
    E1 --> E2[Prevent Token Race]
```

**Concurrency Limits:**
- **Salesforce API**: 25 concurrent requests per org (default)
- **Extension**: No artificial limits, respects Salesforce limits
- **Token Refresh**: Serialized per user to prevent race conditions
- **HTTP Connections**: Connection pool size: 20 (configurable)

---

## Scalability and Limitations

### Salesforce API Limits

```mermaid
graph TB
    A[Salesforce API Limits] --> B[Daily API Limits]
    A --> C[Concurrent Request Limits]
    A --> D[Governor Limits]
    A --> E[Data Limits]

    B --> B1[Enterprise: 1,000 + 1,000 per user]
    B --> B2[Unlimited: 5,000 + 5,000 per user]
    B --> B3[Developer: 15,000 total]

    C --> C1[25 concurrent requests default]
    C --> C2[Configurable up to 100]

    D --> D1[SOQL Queries: 100 per transaction]
    D --> D2[DML Statements: 150 per transaction]
    D --> D3[Query Rows: 50,000 per query]
    D --> D4[CPU Time: 10s sync, 60s async]
    D --> D5[Heap Size: 6MB sync, 12MB async]

    E --> E1[Max Query Result: 50,000 records]
    E --> E2[Max Batch Size: 200 records]
    E --> E3[Max SOQL Length: 20,000 chars]
```

### Extension Limitations

| Limitation Type | Limit | Mitigation Strategy |
|-----------------|-------|---------------------|
| **API Rate Limits** | Varies by Salesforce edition | Monitor usage, implement caching, use bulk operations |
| **Query Result Size** | 50,000 records per query | Use pagination, async queries, or Bulk API |
| **Query Timeout** | 120 seconds | Optimize queries, use selective filters |
| **Concurrent Requests** | 25 (Salesforce default) | Serialize requests if needed, request limit increase |
| **Token Expiration** | 1-2 hours (access token) | Automatic refresh, transparent to users |
| **Refresh Token Expiration** | 90 days (unverified apps) | Re-authentication required |
| **SOQL Query Length** | 20,000 characters | Simplify queries, use dynamic SOQL |
| **Batch Size** | 200 records per batch | Use Bulk API for larger operations |
| **File Upload Size** | 37.5 MB per file | Split large files |
| **Email Recipients** | 5,000 per day | Batch email operations |

### Scalability Patterns

```mermaid
graph LR
    A[Scalability Patterns] --> B[Horizontal Scaling]
    A --> C[Vertical Scaling]
    A --> D[Caching]
    A --> E[Async Processing]

    B --> B1[Multiple Extension Instances]
    B --> B2[Load Balancing]
    B --> B3[Distributed Token Store]

    C --> C1[Increase Memory]
    C --> C2[Increase CPU]
    C --> C3[Optimize Queries]

    D --> D1[Token Caching]
    D --> D2[Metadata Caching]
    D --> D3[Reference Data Caching]

    E --> E1[Async Query Execution]
    E --> E2[Background Jobs]
    E --> E3[Event-Driven Processing]
```

### Performance Monitoring

**Key Metrics to Monitor:**

1. **API Usage**
   - Daily API calls consumed
   - API calls remaining
   - Peak usage times
   - Rate limit violations

2. **Response Times**
   - Average query latency
   - P95/P99 latency
   - Timeout occurrences
   - Slow query identification

3. **Error Rates**
   - Authentication failures
   - Permission errors
   - Salesforce errors (500)
   - Network timeouts

4. **Token Management**
   - Token refresh frequency
   - Token refresh failures
   - Re-authentication events

5. **Resource Utilization**
   - Memory consumption
   - CPU usage
   - HTTP connection pool usage
   - Thread pool utilization

### Capacity Planning

```mermaid
graph TD
    A[Capacity Planning] --> B[Estimate API Usage]
    A --> C[Calculate Limits]
    A --> D[Plan for Growth]

    B --> B1[Users × Requests/Day]
    B --> B2[Automated Workflows]
    B --> B3[Peak Load Scenarios]

    C --> C1[Daily API Limit]
    C --> C2[Concurrent Request Limit]
    C --> C3[Governor Limits]

    D --> D1[User Growth Rate]
    D --> D2[Feature Expansion]
    D --> D3[Integration Growth]

    B1 --> E[Total API Calls/Day]
    B2 --> E
    B3 --> E

    E --> F{Within Limits?}
    F -->|Yes| G[Proceed]
    F -->|No| H[Optimize or Upgrade]

    H --> H1[Implement Caching]
    H --> H2[Use Bulk Operations]
    H --> H3[Upgrade Salesforce Edition]
```

**Capacity Planning Formula:**

```
Daily API Calls = (Users × Avg Requests/User/Day) + (Automated Workflows × Frequency)

Required API Limit = Daily API Calls × 1.5 (safety margin)

Concurrent Requests Needed = Peak Requests/Minute ÷ 60 × Avg Response Time (seconds)
```

**Example:**
- 100 users × 50 requests/day = 5,000 calls
- 10 automated workflows × 100 calls/day = 1,000 calls
- Total: 6,000 calls/day
- With 1.5x safety margin: 9,000 calls/day required
- Salesforce Enterprise (1,000 + 1,000 × 100 users = 101,000) ✅ Sufficient

---

## Security Architecture

### Security Layers

```mermaid
graph TB
    A[Security Architecture] --> B[Authentication Layer]
    A --> C[Authorization Layer]
    A --> D[Transport Security]
    A --> E[Data Protection]
    A --> F[Audit & Logging]

    B --> B1[OAuth 2.0]
    B --> B2[Token Management]
    B --> B3[Session Management]

    C --> C1[Salesforce Permissions]
    C --> C2[Field-Level Security]
    C --> C3[Object-Level Security]
    C --> C4[Sharing Rules]

    D --> D1[HTTPS/TLS 1.2+]
    D --> D2[Certificate Validation]
    D --> D3[Secure Headers]

    E --> E1[Encrypted Credentials]
    E --> E2[Secure Token Storage]
    E --> E3[No Sensitive Data Logging]

    F --> F1[Request Logging]
    F --> F2[Error Logging]
    F --> F3[Audit Trail]
```

### OAuth 2.0 Security

**Security Features:**
1. **Authorization Code Flow**: Most secure flow for user authentication
2. **Client Credentials**: Secure service account authentication
3. **Token Expiration**: Access tokens expire after 1-2 hours
4. **Refresh Token Rotation**: New refresh tokens issued on refresh
5. **Scope Limitation**: Tokens limited to requested scopes
6. **PKCE Support**: Proof Key for Code Exchange (optional)

**Token Storage Security:**
```mermaid
flowchart LR
    A[Token Received] --> B{Token Type}

    B -->|Access Token| C[In-Memory Cache]
    B -->|Refresh Token| D[Encrypted Storage]

    C --> C1[Expires: 1-2 hours]
    C1 --> C2[Not Persisted]

    D --> D1[Encrypted at Rest]
    D1 --> D2[Per-User Isolation]
    D2 --> D3[Secure Retrieval]
```

### Data Protection

| Data Type | Protection Method | Storage Location | Access Control |
|-----------|-------------------|------------------|----------------|
| Client Secret | Encrypted | Extension Configuration | Admin only |
| Password | Encrypted | Extension Configuration | Admin only |
| Security Token | Encrypted | Extension Configuration | Admin only |
| Access Token | In-Memory | Runtime Cache | Per-user isolation |
| Refresh Token | Encrypted | Secure Store | Per-user isolation |
| API Responses | Not Stored | Transient | Request scope |
| Logs | Sanitized | Log Files | Admin only |

### Secure Communication

```mermaid
sequenceDiagram
    participant Extension
    participant TLS
    participant Salesforce

    Extension->>TLS: Initiate HTTPS Connection
    TLS->>TLS: Validate Certificate
    TLS->>Salesforce: Establish Secure Channel

    Extension->>TLS: Send Request (Encrypted)
    TLS->>Salesforce: Forward Request

    Salesforce->>TLS: Send Response (Encrypted)
    TLS->>Extension: Forward Response

    Note over Extension,Salesforce: All communication encrypted with TLS 1.2+
```

**Security Headers:**
- `Authorization: Bearer {token}` - OAuth token
- `Content-Type: application/json` - JSON content
- `Accept: application/json` - JSON response
- `User-Agent: Krista-Salesforce-Extension/2.0.15` - Client identification

### Permission Model

```mermaid
graph TD
    A[User Request] --> B{Authenticated?}
    B -->|No| C[Reject - 401]
    B -->|Yes| D{Has API Access?}

    D -->|No| E[Reject - 403]
    D -->|Yes| F{Object Permission?}

    F -->|No| G[Reject - 403]
    F -->|Yes| H{Field Permission?}

    H -->|No| I[Reject - 403]
    H -->|Yes| J{Sharing Rules?}

    J -->|No| K[Reject - 403]
    J -->|Yes| L[Allow Request]

    L --> M[Execute Operation]
    M --> N[Return Result]
```

**Required Salesforce Permissions:**
- API Enabled
- Read/Write access to objects (Opportunity, Lead, Account, etc.)
- Field-level security for all accessed fields
- Sharing rules allowing record access
- Custom field access (if using custom fields)
- Send Email permission (for email operations)

### Security Best Practices

1. **Credential Management**
   - Never log sensitive credentials
   - Use encrypted storage for secrets
   - Rotate credentials regularly
   - Use separate credentials for dev/prod

2. **Token Management**
   - Store refresh tokens securely
   - Implement token expiration
   - Clear tokens on logout
   - Monitor for token theft

3. **API Security**
   - Validate all inputs
   - Sanitize SOQL queries
   - Use parameterized queries
   - Implement rate limiting

4. **Audit & Compliance**
   - Log all API requests
   - Track authentication events
   - Monitor for suspicious activity
   - Maintain audit trail

5. **Network Security**
   - Use HTTPS only
   - Validate SSL certificates
   - Implement connection timeouts
   - Use secure cipher suites

---

## Deployment Architecture

### Deployment Topology

```mermaid
graph TB
    subgraph "Krista Platform"
        KP[Krista Application Server]
        EXT[Salesforce Sales Extension]
        STORE[Token Store]
        CACHE[Cache Layer]
    end

    subgraph "Network Layer"
        LB[Load Balancer]
        FW[Firewall]
    end

    subgraph "Salesforce Cloud"
        SF[Salesforce API]
        OAUTH[OAuth Server]
        DATA[Salesforce Data]
    end

    subgraph "Monitoring"
        LOG[Log Aggregation]
        METRICS[Metrics Collection]
        ALERT[Alerting]
    end

    KP --> EXT
    EXT --> STORE
    EXT --> CACHE

    EXT --> LB
    LB --> FW
    FW --> SF
    FW --> OAUTH
    SF --> DATA

    EXT --> LOG
    EXT --> METRICS
    METRICS --> ALERT

    style EXT fill:#e1f5ff
    style SF fill:#e1ffe1
    style OAUTH fill:#e1ffe1
```

### Deployment Models

#### Single Instance Deployment

```mermaid
graph LR
    A[Users] --> B[Krista Server]
    B --> C[Extension Instance]
    C --> D[Salesforce]

    style C fill:#e1f5ff
```

**Characteristics:**
- Simple deployment
- Single point of failure
- Limited scalability
- Suitable for: Small teams, development, testing

#### High Availability Deployment

```mermaid
graph TB
    A[Users] --> B[Load Balancer]

    B --> C1[Krista Server 1]
    B --> C2[Krista Server 2]
    B --> C3[Krista Server N]

    C1 --> D1[Extension Instance 1]
    C2 --> D2[Extension Instance 2]
    C3 --> D3[Extension Instance N]

    D1 --> E[Shared Token Store]
    D2 --> E
    D3 --> E

    D1 --> F[Salesforce]
    D2 --> F
    D3 --> F

    style D1 fill:#e1f5ff
    style D2 fill:#e1f5ff
    style D3 fill:#e1f5ff
```

**Characteristics:**
- High availability
- Horizontal scalability
- Shared token store required
- Suitable for: Production, large teams, enterprise

### Environment Configuration

| Environment | Purpose | Configuration | Salesforce Org |
|-------------|---------|---------------|----------------|
| **Development** | Development and testing | sandbox=true, debug logging | Developer Sandbox |
| **Staging** | Pre-production testing | sandbox=true, info logging | Full Sandbox |
| **Production** | Live operations | sandbox=false, warn logging | Production Org |

### Deployment Checklist

**Pre-Deployment:**
- [ ] Salesforce Connected App created and configured
- [ ] OAuth scopes configured (api, refresh_token)
- [ ] Redirect URI configured
- [ ] Client ID and Client Secret obtained
- [ ] Service account credentials configured (if using)
- [ ] Firewall rules configured for Salesforce IPs
- [ ] SSL certificates validated
- [ ] Extension version tested in staging

**Deployment:**
- [ ] Deploy extension to Krista platform
- [ ] Configure extension attributes
- [ ] Test connection to Salesforce
- [ ] Verify authentication flow
- [ ] Test catalog requests
- [ ] Monitor logs for errors
- [ ] Verify token refresh mechanism

**Post-Deployment:**
- [ ] Monitor API usage
- [ ] Monitor error rates
- [ ] Monitor performance metrics
- [ ] Set up alerting
- [ ] Document configuration
- [ ] Train users
- [ ] Establish support process

### Disaster Recovery

```mermaid
flowchart TD
    A[Disaster Scenario] --> B{Type?}

    B -->|Extension Failure| C[Restart Extension]
    B -->|Salesforce Outage| D[Wait for Recovery]
    B -->|Token Store Failure| E[Re-authenticate Users]
    B -->|Network Failure| F[Check Connectivity]

    C --> G{Success?}
    G -->|Yes| H[Resume Operations]
    G -->|No| I[Escalate to Support]

    D --> J[Monitor Salesforce Status]
    J --> K{Recovered?}
    K -->|Yes| H
    K -->|No| L[Notify Users]

    E --> M[Clear Token Cache]
    M --> N[Trigger Re-auth]
    N --> H

    F --> O[Check Firewall]
    O --> P[Check DNS]
    P --> Q[Check SSL]
    Q --> H
```

**Recovery Time Objectives (RTO):**
- Extension restart: < 5 minutes
- Token store recovery: < 15 minutes
- Full disaster recovery: < 1 hour

**Recovery Point Objectives (RPO):**
- No data loss (stateless extension)
- Token refresh required after recovery

### Monitoring and Observability

**Logging Strategy:**
```mermaid
graph LR
    A[Extension] --> B[Log Events]

    B --> C[DEBUG]
    B --> D[INFO]
    B --> E[WARN]
    B --> F[ERROR]

    C --> G[Development Only]
    D --> H[All Environments]
    E --> H
    F --> H

    H --> I[Log Aggregation]
    I --> J[Analysis & Alerting]
```

**Key Log Events:**
- Authentication attempts (success/failure)
- Token refresh events
- API requests (method, endpoint, duration)
- Errors (with stack traces)
- Performance metrics (latency, throughput)
- Configuration changes

**Alerting Rules:**
- Authentication failure rate > 10%
- API error rate > 5%
- Average latency > 5 seconds
- Token refresh failure rate > 5%
- Daily API limit > 80% consumed

---

## Summary

The Salesforce Sales Extension implements a robust, scalable, and secure architecture that provides seamless integration between Krista and Salesforce Sales Cloud. Key architectural highlights:

### Architectural Strengths

1. **5-Layer Architecture**: Clear separation of concerns with well-defined responsibilities
2. **OAuth 2.0 Security**: Industry-standard authentication with automatic token refresh
3. **Graceful Error Handling**: User-friendly error messages with comprehensive logging
4. **Performance Optimized**: Query optimization, caching, and connection pooling
5. **Scalable Design**: Horizontal scaling support with shared token store
6. **Comprehensive Monitoring**: Detailed logging and metrics for observability

### Design Patterns Used

- **Repository Pattern**: DAO layer abstracts data access
- **Factory Pattern**: Token provider factory for different auth modes
- **Dependency Injection**: Constructor injection throughout
- **Strategy Pattern**: Different authentication strategies (user vs admin)
- **Singleton Pattern**: Extension instance and managers
- **Builder Pattern**: Query builders for complex SOQL

### Technology Decisions

| Decision | Rationale |
|----------|-----------|
| Java 21 | Latest LTS version with modern features |
| OAuth 2.0 | Industry standard, secure, widely supported |
| REST API v58.0+ | Latest Salesforce API with full feature support |
| Stateless Design | Enables horizontal scaling and high availability |
| Interface-Based DAOs | Testability and flexibility |
| Graceful Degradation | Better user experience on errors |

### Future Enhancements

- Bulk API support for large data operations
- Streaming API for real-time events
- Metadata API for schema introspection
- Enhanced caching with Redis/Memcached
- Circuit breaker pattern for resilience
- GraphQL support for flexible queries

---

**Document Version:** 1.0
**Last Updated:** 2025-11-27
**Maintained By:** Krista Extension Team
