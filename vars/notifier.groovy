def call(){
    sh """
        curl -X POST -H 'Content-type: application/json' \
        --data '{"text":"🚀 Pipeline ${status} for ${appName}!"}' \
        https://httpbin.org
        """
}