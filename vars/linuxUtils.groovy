def printHeader(String title) {
    sh "echo '========================================'"
    sh "echo '🚀 ${title.toUpperCase()}'"
    sh "echo '========================================'"
}

def checkTool(String toolName) {
    def statusCode = sh(script: "command -v ${toolName}", returnStatus: true)    
    if (statusCode == 0) {
        echo "✅ SUCCESS: ${toolName} is available in this Jenkins pod."
    } else {
        echo "❌ WARNING: ${toolName} is NOT installed in this Jenkins pod."
    }
}