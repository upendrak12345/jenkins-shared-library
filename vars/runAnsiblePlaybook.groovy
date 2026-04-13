def call(Map config) {
    def playbookPath  = config.playbook
    def inventory     = config.inventory      ?: 'inventory/hosts'
    def edgeNode      = config.edgeNode       ?: 'edge-node-01'
    def credId        = config.credentialId   ?: 'ansible-ssh-key'  // ✅ Bug 2 fixed
    def extraVars     = config.extraVars      ?: [:]
    def extraVarsStr  = extraVars.collect { k, v -> "-e ${k}=${v}" }.join(' ')
    def dryRun        = config.dryRun         ?: false
    def syntaxCheck   = config.syntaxCheck    ?: false
    def become        = config.become         ?: false
    def askPass       = config.askPass        ?: false
    def askBecomePass = config.askBecomePass  ?: false
    def tags          = config.tags           ?: ''
    def skipTags      = config.skipTags       ?: ''
    def limit         = config.limit          ?: ''
    def forks         = config.forks          ?: ''
    def verbosity     = config.verbosity      ?: ''

    def flags = []  // ✅ Bug 1 fixed
    if (dryRun)        flags << '--check'
    if (syntaxCheck)   flags << '--syntax-check'
    if (become)        flags << '--become'
    if (askPass)       flags << '--ask-pass'
    if (askBecomePass) flags << '--ask-become-pass'
    if (tags)          flags << "--tags ${tags}"
    if (skipTags)      flags << "--skip-tags ${skipTags}"
    if (limit)         flags << "--limit ${limit}"
    if (forks)         flags << "--forks ${forks}"
    if (verbosity)     flags << "-${verbosity}"

    def flagsStr  = flags.join(' ')
    def ansibleCmd = "ansible-playbook /tmp/playbooks/${playbookPath} -i ${inventory} ${extraVarsStr} ${flagsStr}".trim()

    pipeline {
        agent any
        stages {
            stage('Copy Playbook to Edge Node') {
                steps {
                    script {
                        sh """
                            echo "scp -o StrictHostKeyChecking=no \
                                ${playbookPath} \
                                ansible@${edgeNode}:/tmp/playbooks/"
                        """
                    }
                }
            }
            stage('Run Playbook on Edge Node') {
                steps {
                    script {
                        sh "echo \"ssh -o StrictHostKeyChecking=no ansible@${edgeNode} '${ansibleCmd}'\""  // ✅ Bug 3 fixed
                    }
                }
            }
        }
    }
}