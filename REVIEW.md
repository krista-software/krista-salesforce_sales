# Code Review: Salesforce Sales Extension

## Scope & Reviewer Guidelines

- Exclusions (do not flag): Authentication flow logic errors, SOQL injection vulnerabilities, missing error-handling tests, assert usage in production code
- Focus areas: Code quality improvements, minor security hardening, performance optimizations, documentation improvements, optional refactoring opportunities
- Decision: Per request, no code changes will be applied now; this review records findings and recommended fixes for later

## High-Priority Summary (No changes applied now)

1) SOQLDaoImpl.parseQueryResult null-safety and generics
- Risk: NPE when `records` is null; raw types reduce safety
- Fix: Null checks for `response`/`records` and parse with typed `TypeToken`

2) QuoteDaoImpl.filterQuoteLineItem multi-key correctness
- Risk: Only first key is evaluated; multi-criteria filters fail
- Fix: Evaluate all entries and require every key to match

## 1. Issue Analysis by Category

### 🧠 Correctness

1) Possible NPE in SOQLDaoImpl.parseQueryResult when `records` is null
- Problem: The code assumes `response.records` is non-null and calls `size()` directly.
- Impact: NPE at runtime (we reproduced this during testing before fixing the test JSON shape).
- Location:
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/SOQLDaoImpl.java" mode="EXCERPT">
````java
public static Map parseQueryResult(List results, String res) {
    SalesforceListResponse response = GSON.fromJson(res, SalesforceListResponse.class);
    if (response.records.size() > 0) {
        for (Object map : response.records) {
            // ...
        }
    } else {
        return new HashMap<>();
    }
}
````
</augment_code_snippet>
- Suggested fix (null-safe, with generics):
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/SOQLDaoImpl.java" mode="EXCERPT">
````java
public static Map<String, Object> parseQueryResult(List<Map<String, Object>> results, String res) {
    SalesforceListResponse<Map<String, Object>> response =
        GSON.fromJson(res, new com.google.gson.reflect.TypeToken<SalesforceListResponse<Map<String, Object>>>(){}.getType());
    if (response == null || response.records == null || response.records.isEmpty()) {
        return new HashMap<>();
    }
    // process records ...
}
````
</augment_code_snippet>

2) QuoteDaoImpl.filterQuoteLineItem only checks the first key
- Problem: It returns inside the loop on the first key; multi-key filters won't work as intended.
- Location:
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/QuoteDaoImpl.java" mode="EXCERPT">
````java
private static boolean filterQuoteLineItem(Map filter, Map<String, Object> quoteLineItem) {
    for (Object key : filter.keySet()) {
        return quoteLineItem.get(key) != null &&
               String.valueOf(quoteLineItem.get(key)).equals(((Map<?, ?>) filter).get(key));
    }
    return false;
}
````
</augment_code_snippet>
- Suggested fix: check all keys
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/QuoteDaoImpl.java" mode="EXCERPT">
````java
private static boolean filterQuoteLineItem(Map<String, ?> filter, Map<String, Object> item) {
    for (Map.Entry<String, ?> e : filter.entrySet()) {
        Object v = item.get(e.getKey());
        if (v == null || !String.valueOf(v).equals(String.valueOf(e.getValue()))) {
            return false;
        }
    }
    return true;
}
````
</augment_code_snippet>

3) ContentDaoImpl null check uses `value.equals(null)`
- Problem: Uses `value.equals(null)` instead of `value == null`/`JSONObject.NULL`. Risky and unconventional.
- Location:
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/stores/ContentDaoImpl.java" mode="EXCERPT">
````java
Object value = record.get(key);
if (value.equals(null)) {
    resultMap.put(key, "Text", "");
} else {
    resultMap.put(key, "Text", value.toString());
}
````
</augment_code_snippet>
- Suggested fix (org.json handling):
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/stores/ContentDaoImpl.java" mode="EXCERPT">
````java
Object value = record.get(key);
if (value == null || value == org.json.JSONObject.NULL) {
    resultMap.put(key, "Text", "");
} else {
    resultMap.put(key, "Text", String.valueOf(value));
}
````
</augment_code_snippet>

4) Inconsistent exception types for validation
- Problem: AssetDaoImpl.updateAsset throws RuntimeException for empty id; other DAOs throw IllegalArgumentException.
- Suggestion: Standardize to IllegalArgumentException to communicate caller error consistently.
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/AssetDaoImpl.java" mode="EXCERPT">
````java
if (asset.getId() == null || asset.getId().isEmpty())
    throw new RuntimeException("Asset Id cant be empty");
````
</augment_code_snippet>
- Fix:
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/AssetDaoImpl.java" mode="EXCERPT">
````java
if (asset.getId() == null || asset.getId().isEmpty()) {
    throw new IllegalArgumentException("Asset Id cant be empty");
}
````
</augment_code_snippet>


### ⚙️ OOP Principles

- @Inject constructors marked `private` (e.g., QuoteManagementArea, ReportingArea). HK2 can reflectively access private constructors, but using package-private/public improves clarity, testability, and DI portability.
- Several methods return or accept raw types (e.g., `Map`, `List`) which weakens type safety. Prefer generics, especially in parsing and filtering methods.
- DAO responsibilities are cleanly separated; however, repeated query-building logic across DAOs could be abstracted (e.g., a small SOQL builder/helper).


### 📏 Code Quality & Best Practices

- Deprecated Gson API usage
  - QuoteDaoImpl uses `new JsonParser().parse(res)`; replace with `JsonParser.parseString(res)` or rely on a single Gson instance (already present as `GSON`).
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/QuoteDaoImpl.java" mode="EXCERPT">
````java
JsonObject object = new JsonParser().parse(res).getAsJsonObject();
````
</augment_code_snippet>
- Fix:
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/QuoteDaoImpl.java" mode="EXCERPT">
````java
JsonObject object = com.google.gson.JsonParser.parseString(res).getAsJsonObject();
````
</augment_code_snippet>

- ExecutorService lifecycle in ReportingArea
  - A new single-thread executor is created and never shut down. Consider injecting a managed executor or exposing lifecycle hooks.
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/catalog/ReportingArea.java" mode="EXCERPT">
````java
ExecutorService executorService = Executors.newSingleThreadExecutor();
// ... never shutdown()
````
</augment_code_snippet>
- Suggested improvement: inject an ExecutorService or use a shared managed executor and add shutdown on service stop.

- Naming: SalesforceManager.getoAuthRequest should be `getOAuthRequest` to match Java conventions.
<augment_code_snippet path="salesforce_sales/src/main/java/app/krista/extensions/crm/sales/salesforce_sales/impl/SalesforceManager.java" mode="EXCERPT">
````java
private OAuthRequest getoAuthRequest(Verb verb, String body, SalesforceToken token, String url) {
````
</augment_code_snippet>

- Mixed error signaling: SalesforceManager returns empty strings on network errors. Prefer exceptions or Optional to avoid silent failures.

- Logging levels: Some `info` logs are more diagnostic (e.g., ContentArea non-custom field). Consider `debug` for noisy logs and `warn` for user/input issues.

- Date/time: DatePlus uses deprecated java.util.Date APIs; consider java.time for correctness and readability.


### 🔒 Security

- Avoid logging raw exception messages from Salesforce responses if they could contain sensitive info; sanitize where practical.
- Hardcoded API versions are acceptable but consider centralizing or reading from configuration for easier upgrades.
- SOQL injection review is accepted; no action required.


### 🧬 Code Duplication

- Repeated patterns across DAO methods for building SOQL strings and converting list responses. Consider:
  - A small utility for SELECT/LIMIT/OFFSET building
  - A generic `listResponseToEntities(records, mapper)` helper to reduce boilerplate


### ⚠️ Regression Risk

- Changing parsing in SOQLDaoImpl and QuoteDaoImpl (Gson updates) could alter behavior if downstream relies on current shapes. Mitigation: add/keep tests around these code paths (see Unit Test suggestions below).
- Adjusting exception types can surface errors earlier; verify callers handle IllegalArgumentException consistently.


### 🚀 Performance

- Current hotspots are minimal; the asynchronous reporting task spawns a dedicated thread per class instance. Switching to an injected/shared executor avoids thread proliferation.
- String building in ContentArea is fine for small field lists.


### 🧪 Unit Test Coverage (focus on gaps only)

- Add edge-case tests:
  - SOQLDaoImpl.parseQueryResult when `records` is null or empty
  - QuoteDaoImpl.filterQuoteLineItem with multi-key filters
  - ContentDaoImpl JSON with explicit `JSONObject.NULL` values
  - SalesforceManager.getErrorMessage branches (empty array, missing fields)


### 🗒️ Comments & Documentation

- Add concise Javadoc to public Catalog Area methods summarizing behavior and notable constraints (e.g., custom fields must end with `__c`).
- Document JSON response expectations where parsing occurs (SOQLDaoImpl.parseQueryResult, ContentDaoImpl.processJSONResponse).
- Consider class-level comments clarifying DI expectations (constructor visibility).


## 2. Priority Summary Table


| Priority | Count | Time Estimate |
|----------|-------|---------------|
| 🔴 Critical | 0 | ~0h |
| 🟡 High | 0 | ~0h |
| 🟢 Medium | 5 | ~6–10h |
| 🔵 Low | 6 | ~6–8h |
| **TOTAL** | **11** | **~12–18h** |

Notes:
- High: None
- Medium: ContentDaoImpl null handling; ExecutorService lifecycle; Deprecated Gson API modernization; Exception type consistency; Naming.
- Low: Logging polish; duplication refactors; DatePlus modernization; error signaling consistency; docs/Javadoc; centralizing API versions.


## 3. Overall Assessment

⚠️ Approve with required fixes (high priority issues that don't block deployment)

The codebase is structurally sound with clear separation of concerns and now strong unit test coverage. A couple of correctness issues (parser null-safety, multi-key filtering) should be addressed soon; they are localized and low-risk to fix, especially with the existing tests.


## 4. Action Items

1) Fix SOQLDaoImpl parseQueryResult null-safety and add targeted tests
- Implement generics and records null-checks
- Add tests for null/empty/malformed `records`

2) Correct QuoteDaoImpl.filterQuoteLineItem to evaluate all keys
- Update implementation to check all provided filter keys
- Add multi-key positive/negative tests

3) Improve ContentDaoImpl null handling
- Replace `value.equals(null)` with `value == null || value == JSONObject.NULL`
- Add test for JSONObject.NULL

4) ExecutorService lifecycle in ReportingArea
- Inject a shared executor or add a managed shutdown
- Optionally, switch to CompletableFuture.supplyAsync with injected executor

5) Modernize Gson API usage and remove deprecations
- Replace JsonParser constructor usage with `JsonParser.parseString`
- Run build to ensure warnings are resolved

6) Normalize exception types and naming
- Use IllegalArgumentException for validation errors in DAOs
- Rename getoAuthRequest -> getOAuthRequest (and references)

7) Documentation and logging polish
- Add Javadoc to public Catalog Area methods and key DAOs
- Review log levels; reduce info-noise, promote warnings for user errors

8) Optional refactors for duplication and date/time
- Introduce a minimal SOQL builder/helper
- Move to java.time in DatePlus (non-functional change)

