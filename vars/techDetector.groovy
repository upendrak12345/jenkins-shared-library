def detectLanguage() {
    // -f checks if a file exists in the current directory
    def isNode = sh(script: "test -f package.json", returnStatus: true)
    def isPython = sh(script: "test -f requirements.txt", returnStatus: true)

    // In Unix, an exit code of 0 means true/success
    if (isNode == 0) {
        return "node"
    } else if (isPython == 0) {
        return "python"
    } else {
        return "unknown"
    }
}