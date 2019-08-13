if (JENKINS_URL == 'https://ci.jenkins.io/') {
    buildPlugin(
      configurations: buildPlugin.recommendedConfigurations().findAll { it.platform == 'linux' },
    )
    return
}
