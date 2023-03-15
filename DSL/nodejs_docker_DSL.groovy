job("Node JS Job with DSL"){

    description("This app was coded in nodeJS and buildede in Jenkins with DSL")

    scm{
        git("https://github.com/macloujulian/nodejsapp.git", "master"){ node ->
            node / gitConfigName("Marco Antonio AG")
            node / gitConfigEmail("marcontonio.98@hotmail.com")
        }
    }

    //Each 7 minutes jenkins will check if there are changes in the
    //git repository, if there are some change it will build it auto
    //matically.
    triggers{
        scm("H/7 * * * *")
    }

    wrappers{
        nodejs("nodejs-jenkins")
    }
    
    steps{

        dockerBuildAndPublish{
            repositoryName("boxoky/nodejsapp")
            tag('${GIT_REVISION,length=7}"'
            registryCredentials("docker-hub")
            forcePull(false)
            createFingerprints(false)
            skipDecorate()
        }
    }

    publishers{
        //Notifiers (slack messages)
        mailer('marcontonio.98@hotmail.com', false, true)
        slackNotifier {
            notifyAborted(true)
            notifyEveryFailure(true)
            notifyNotBuilt(true)
            notifyUnstable(false)
            notifyBackToNormal(true)
            notifySuccess(true)
            notifyRepeatedFailure(false)
            startNotification(false)
            includeTestSummary(false)
            includeCustomMessage(false)
            customMessage(null)
            sendAs(null)
            commitInfoChoice('NONE')
            teamDomain(null)
            authToken(null)
        }
    }
}