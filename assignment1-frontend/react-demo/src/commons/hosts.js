export const HOST = {
    user_api: process.env.REACT_APP_HOST_USER_API || 'http://localhost:8080',
    device_api: process.env.REACT_APP_HOST_DEVICE_API || 'http://localhost:8081',
    monitoring_api: process.env.REACT_APP_HOST_MONITORING || 'http://localhost:8082',
    chat_api: process.env.REACT_APP_HOST_CHAT || 'http://localhost:8083',

};

export const endpoint = {
    user: '/user-api/user',
    device: '/device-api/device',
    login: '/user-api/login',
    monitoring: '/monitoring-api/device',
    ws : '/ws'
};

