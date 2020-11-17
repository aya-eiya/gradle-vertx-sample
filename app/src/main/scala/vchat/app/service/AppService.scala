package vchat.app.service

import vchat.app.env.AppEnvMap
import vchat.server.Service

trait AppService extends Service with AppEnvMap
