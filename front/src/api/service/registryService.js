import restApi from '../restApi';

export default {


    getAllEnvironments() {
        let url = '/api/env/all';
        return restApi.doGetRequest(url);
    },
    getAllOrgs() {
        let url = '/api/org/all';
        return restApi.doGetRequest(url);
    },
    getAllUsers() {
        let url = '/api/user/all';
        return restApi.doGetRequest(url);
    },
    getUsersByPage(request) {
        let url = '/api/users?page=' + request.page + '&size=' + request.size + '&realName=' + request.realName + '&name=' + request.name +  '&orgCode=' + request.shortCode;
        return restApi.doGetRequest(url);
    },
    getAllRepos() {
        let url = '/api/repo/all';
        return restApi.doGetRequest(url);
    },
    getReposByPage(request) {
        let url = '/api/repos?name=' + request.query.name + '&orgId=' + request.query.orgId + '&orgName=' + request.query.orgName + '&page=' + request.page + '&size=' + request.size;
        return restApi.doGetRequest(url);
    },
    getReposByOrg(request) {
        let url = '/api/repos/condition?orgId=' + request;
        return restApi.doGetRequest(url);
    },
    getReposByOrgName(request) {
        let url = '/api/repos/condition?orgName=' + request;
        return restApi.doGetRequest(url);
    },
    getAllImages() {
        let url = '/api/image/all';
        return restApi.doGetRequest(url);
    },
    //这里暂时加repoId，repoName两个参数。
    getImagesByPage(request) {
        let url = '/api/images?orgId=' + request.query.orgId + '&orgName=' + request.query.orgName + '&repoId=' + request.query.repoId + '&page=' + request.page + '&size=' + request.size;
        return restApi.doGetRequest(url);
    },
    //这里是个什么功能？(可否用/imgextds?imageId= 来代替)
    getImageTags() {
        let url = '/api/imagetags/sortbyenv';
        return restApi.doGetRequest(url);
    },
    //这里是个什么功能？
    getImageTagsByPage(request) {
        let url = '/api/imgextds?orgId=' + request.query.orgId + '&orgName=' + request.query.orgName + '&repoId=' + request.query.repoId + '&page=' + request.page + '&size=' + request.size;
        return restApi.doGetRequest(url);
    },
    //这里改成了put请求，注意参数是否能传过来
    updateImageState(request) {
        let url = '/api/imgextd/value?imageId='+ request.imageId + '&envName=' + request.envName + '&value=' + request.imageState;
        return restApi.doGetRequest(url, request);
    },
    //这里改成了put请求，注意参数是否能传过来
    updateEnvironment(request) {
        let url = '/api/env';
        return restApi.doPutRequest(url, request);
    },
    deleteEnvironmentById(request) {
        let url = '/api/env/' + request;
        return restApi.doDeleteRequest(url);
    },
    updateOrganization(request) {
        let url = '/api/org';
        return restApi.doPutRequest(url, request);
    },
    deleteOrganizationById(request) {
        let url = '/api/org/' + request;
        return restApi.doDeleteRequest(url);
    },
    deleteRepositoryById(request) {
        let url = '/api/repo/' + request.repoId;
        return restApi.doDeleteRequest(url);
    },
    deleteImageById(request) {
        let url = '/api/image/' + request.imageId;
        return restApi.doDeleteRequest(url);
    },
    syncEnvList(request) {
        let url = '/api/env/sync';
        return restApi.doGetRequest(url);
    },
    syncOrgInfo(request) {
        let url = '/api/orgs/sync';
        return restApi.doGetRequest(url);
    },
    syncUserList(request) {
        let url = '/api/users/sync';
        return restApi.doGetRequest(url);
    },
    getLogsByPage(request) {
        let url = '/api/logs?page=' + request.page + '&size=' + request.size + '&eventName=' + request.eventName + '&eventAddition=' + request.eventAddition + '&optionType=' + request.optionType + '&operator=' + request.operator + '&operateTime=' + request.operateTime;
        return restApi.doGetRequest(url);
    },

    getPushToken(request) {
        let url = null;
        if (request != null && request.redirect_url != null) {
            url = 'api/token/getPushToken?redirect_url=' + encodeURIComponent(request.redirect_url);
        } else {
            url = 'api/login/getPushToken';
        }
        return restApi.doGetRequest(url);
    },


}