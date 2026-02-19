# Split Placeholder Fix - Version 2.0.19

## Problem Statement

Client templates have placeholders (e.g., `$PropAddress`, `$PropertyLegalDescription`) that are **split across multiple XML runs** in the Word document structure:

```xml
<w:r>
  <w:t>$</w:t>
</w:r>
<w:proofErr w:type="spellStart"/>
<w:r>
  <w:t>PropAddres</w:t>
</w:r>
<w:r>
  <w:t>s</w:t>
</w:r>
```

This happens when:
- Word's spell checker flags the placeholder as misspelled
- The user edits the placeholder character by character
- Word creates separate runs for different formatting states

**Result**: Simple string replacement (`xmlContent.replace("$PropAddress", "value")`) fails because the complete placeholder string doesn't exist in the XML.

## Solution

**Route watermarked documents through DOM processing instead of ZIP manipulation.**

### Code Change

**File**: `Docx4jTemplateService.java`

**Before** (Simran's fix):
```java
private java.io.File fillWithDocx4jProcessing(...) {
    if (!hasHtmlContent) {
        return fillWithDirectZipManipulation(...);  // ❌ Can't handle split placeholders
    } else {
        return fillWithDocx4jDomProcessing(...);
    }
}
```

**After** (Our fix):
```java
private java.io.File fillWithDocx4jProcessing(...) {
    // Always use DOM processing for watermarked documents
    return fillWithDocx4jDomProcessing(...);  // ✅ Handles split placeholders
}
```

### Why This Works

The `fillWithDocx4jDomProcessing()` method uses:
1. **`replaceTextInWatermarkParagraph()`** - Replaces text while preserving watermark structure
2. **`performSplitPlaceholderReplacement()`** - Handles placeholders split across multiple runs

The split placeholder handler:
- Collects all text elements from all runs in the paragraph
- Concatenates them into a single string
- Finds and replaces the placeholder in the concatenated string
- Redistributes the result back to the first text element
- Clears the remaining text elements

## Regression Safety Analysis

### ✅ Non-Watermarked Documents
**Impact**: ZERO
- Still routed through `XWPFDocManager` (line 102)
- Completely separate code path
- No changes to this flow

### ✅ Watermarked Documents with HTML Content
**Impact**: ZERO
- Already using `fillWithDocx4jDomProcessing()` before this change
- No change in behavior

### ✅ Watermarked Documents with Plain Text (Non-Split Placeholders)
**Impact**: Minimal, Safe
- **Before**: ZIP manipulation (simple string replace)
- **After**: DOM processing (DOM-based replace)
- **Risk**: Low - DOM processing is more robust, just slightly slower
- **Benefit**: Can now handle split placeholders

### ✅ Watermarked Documents with Plain Text (Split Placeholders)
**Impact**: FIX
- **Before**: Failed (placeholders not replaced)
- **After**: Works (split placeholder handler activates)

## Testing Recommendations

1. **Non-watermarked documents** - Should work exactly as before
2. **Watermarked documents with normal placeholders** - Should work (may be slightly slower)
3. **Watermarked documents with split placeholders** - Should now work (THIS IS THE FIX)
4. **Watermarked documents with HTML content** - Should work exactly as before

## Performance Impact

- **Non-watermarked**: No impact (same code path)
- **Watermarked**: Slight increase in processing time (DOM parsing vs ZIP manipulation)
- **Trade-off**: Correctness > Speed (better to be slightly slower than to fail)

## Deployment

**JAR**: `msword/build/libs/ms-word-2.0.19.jar`

**Version**: 2.0.19

**Date**: 2026-02-17

**Confidence**: HIGH - Uses existing, tested code paths

