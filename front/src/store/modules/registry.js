import {api} from '../../api';

// initial state
const state = {
    environments: null,
    orgs: [],
    users: [],
    userCount: 0,
    imageCount: 0,
    imageTagCount: 0,
    repos: [],
    repoCount: 0,
    queryRepos: [],
    images: [],
    imageTags: [],
    logs: [],
    logCount: 0
};

// getters
const getters = {
    getEnvironments: state => state.environments,
    getOrganizations: state => state.orgs,
    getUsers: state => state.users,
    getUserCount: state => state.userCount,
    getImageCount: state => state.imageCount,
    getImageTagCount: state => state.imageTagCount,
    getRepositories: state => state.repos,
    getRepoCount: state => state.repoCount,
    getQueryRepositories: state => state.queryRepos,
    getImages: state => state.images,
    getImageTags: state => state.imageTags,
    getLogs : state => state.logs,
    getLogCount : state => state.logCount,
};

// mutations
const mutations = {
    saveEnvironments(state, data) {
        state.environments = data;
    },
    saveOrganizations(state, data) {
        state.orgs = data;
    },
    saveUsersByPage(state, data) {
        state.users = data.content;
        state.userCount = data.totalElements;
    },
    saveRepos(state, data) {
        state.repos = data;
    },
    saveReposByPage(state, data) {
        state.repos = data.content;
        state.repoCount = data.totalElements;
    },
    saveQueryRepos(state, data) {
        console.log(data)
        state.queryRepos = data;
    },
    saveImages(state, data) {
        state.images = data;
    },
    saveImagesByPage(state, data) {
        state.images = data.content;
        state.imageCount = data.totalElements;
    },
    saveImageTags(state, data) {
        state.imageTags = data;
    },
    saveImageTagsByPage(state, data) {
        state.imageTags = data.content;
        state.imageTagCount = data.totalElements;
    },
    saveLogs(state, data) {
        state.logs = data.content;
        state.logCount = data.totalElements;
    }
};

// actions
const actions = {

    fetchAllEnvironments({commit}) {
        api.registryService.getAllEnvironments().then(function (res) {
            commit('saveEnvironments', res.data);
        })
    },
    fetchAllOrganizations({commit}) {
        api.registryService.getAllOrgs().then(function (res) {
            commit('saveOrganizations', res.data);
        })
    },
    fetchUsersByPage({commit}, data) {
        api.registryService.getUsersByPage(data).then(function (res) {
            commit('saveUsersByPage', res.data);
        })
    },
    fetchAllRepos({commit}) {
        api.registryService.getAllRepos().then(function (res) {
            commit('saveRepos', res.data);
        })
    },
    fetchReposByPage({commit}, data) {
        api.registryService.getReposByPage(data).then(function (res) {
            commit('saveReposByPage', res.data);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },
    fetchReposByOrg({commit}, data) {
        api.registryService.getReposByOrg(data).then(function (res) {
            commit('saveQueryRepos', res.data);
        })
    },
    fetchReposByOrgName({commit}, data) {
        api.registryService.getReposByOrgName(data).then(function (res) {
            commit('saveQueryRepos', res.data);
        })
    },
    fetchAllImages({commit}) {
        api.registryService.getAllImages().then(function (res) {
            commit('saveImages', res.data);
        })
    },
    fetchImagesByPage({commit}, data) {
        api.registryService.getImagesByPage(data).then(function (res) {
            commit('saveImagesByPage', res.data);
        })
    },
    fetchImageTags({commit}) {
        api.registryService.getImageTags().then(function (res) {
            commit('saveImageTags', res.data);
        })
    },
    fetchImageTagsByPage({commit}, data) {
        api.registryService.getImageTagsByPage(data).then(function (res) {
            commit('saveImageTagsByPage', res.data);
        })
    },
    updateImageState({commit, dispatch}, data) {
        api.registryService.updateImageState(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchImageTagsByPage', data.queryParam);
        })
    },
    updateEnvironment({commit, dispatch}, data) {
        api.registryService.updateEnvironment(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchAllEnvironments');
        })
    },
    deleteEnvironmentById({commit, dispatch}, data) {
        api.registryService.deleteEnvironmentById(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchAllEnvironments');
        })
    },
    updateOrganization({commit, dispatch}, data) {
        api.registryService.updateOrganization(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchAllOrganizations');
        })
    },
    deleteOrganizationById({commit, dispatch}, data) {
        api.registryService.deleteOrganizationById(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchAllOrganizations');
        })
    },
    deleteRepositoryById({commit, dispatch}, data) {
        api.registryService.deleteRepositoryById(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchReposByPage', data.queryParam);
        })
    },
    deleteImageById({commit, dispatch}, data) {
        api.registryService.deleteImageById(data).then(function (res) {
            console.log(res.data);
            dispatch('fetchImagesByPage', data.queryParam);
        })
    },
    syncEnvList({commit, dispatch}) {
        api.registryService.syncEnvList().then(function (res) {
            dispatch('fetchAllEnvironments');
        })
    },
    syncOrgInfo({commit, dispatch}) {
        api.registryService.syncOrgInfo().then(function (res) {
            dispatch('fetchAllOrganizations');
        })
    },
    syncUserList({commit, dispatch}, data) {
        api.registryService.syncUserList().then(function (res) {
            dispatch('fetchUsersByPage',res.data);
        })
    },
    fetchAllLogs({commit}) {
        api.registryService.getLogsByPage().then(function (res) {
            commit('saveLogs', res.data);
        })
    },
    fetchLogsByPage({commit}, data) {
        api.registryService.getLogsByPage(data).then(function (res) {
            commit('saveLogs', res.data);
        })
    }
};


export default {
    state,
    getters,
    actions,
    mutations
}