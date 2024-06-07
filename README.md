# mobile-android-pipelines
Reusable scripts and pipelines for building, testing and reporting on Android repositories.


The workflow can be called from GitHub actions as a step:

```yaml
steps:
  - name: Run Android workflow
    uses: govuk-one-login/mobile-android-pipelines@main
```

If used as part of a pull request workflow, the pipeline will:
- Checkout the code (both scripts and repository)
- Perform relevant setup of the GitHub runner
- Run static code analysis (such as linting)
- Run unit & instrumetation tests
- Upload results to SonarCloud
- Ensure that SonarCloud results pass to required level

If the GitHub Action was triggered by a different action, such as a merge or workflow dispatch, then the workflow will also:
- Determine the new semantic version number using conventional commits
- Tag the branch as a new release
- Build a new release artefact
- Upload the new release to GitHub Packages
