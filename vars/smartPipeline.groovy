def tech = techDetector.identifyStack() // Calling your function

if (tech == 'node') {
    sh "npm install && npm test"
} else if (tech == 'python') {
    sh "pip install -r requirements.txt && pytest"
} else {
    error "Unsupported project type detected!"
}