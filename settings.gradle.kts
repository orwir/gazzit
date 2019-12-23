rootProject.name = "gazzit"

include(
    ":app",

    ":features:splash",
    ":features:login",
    ":features:feed",

    ":libraries:core",
    ":libraries:view",
    ":libraries:authorization",
    ":libraries:listing",
    ":libraries:videoplayer"
)