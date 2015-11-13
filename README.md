# ssodemo
##结构
* auth :统一认证和统一登录的服务 auth.domain.com,提供 auth认证和login登录地址
* demo1 :二级demo1.domain.com
* demo2 :二级demo2.domain.com
##流程
用户访问demo1网站，demo1用访问domain.com下的cookie，如果有tokenKey把tokenkey发送给auth的认证服务，如果
auth的缓存中有改key，返回登录成功，demo1继续访问，否则跳转到统一登录的地址认证通过后返回demo1的地址。并且把key存在一级域名的cookie中
