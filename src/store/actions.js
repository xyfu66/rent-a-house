import axios from "axios"
import _ from "lodash"

const SERVER_URL = '/api'
const customConfig = {
    headers: {
        'Content-Type': 'multipart/form-data; boundary=<calculated when request is sent>'
    }
}

const actions = {
    /**
     * 重新请求对应表单的历史版本的api
     * @param {string} formId 表单ID
     */
    fetchHistoryVersions({ commit }, formId) {
        return axios.get(SERVER_URL + '/get-content-by-id', { params: { "form-id": formId } }, customConfig)
            .then((res) => {
                if (res.status == 200) {
                    var versionList = res.data["content"][0]
                    commit('historyVersions', versionList)
                    return versionList;
                }
            })
    },

    /**
     * 刷新表单内容
     */
    refreshFormContent({ commit }, playload) {
        var params = {}
        if (!_.isNaN(playload.formId)) {
            params["form-id"] = playload.formId
        }

        if (_.isUndefined(playload.versionId)) {
            params["version-id"] = 0
        } else {
            params["version-id"] = playload.versionId
        }

        return axios.get(SERVER_URL + '/get-form', {
            params: params
        })
            .then((res) => {
                if (res.status == 200) {
                    commit('refreshForm', res)
                }
                return res;
            })
    },

    /**
     * 刷新表头
     */
    fetchHistoryForms({ commit }) {
        return axios.get(SERVER_URL + '/get-all-form-info', {}, customConfig)
            .then((res1) => {
                if (res1.status == 200) {
                    var formInfo = res1.data['form-info-all'][0]
                    if (formInfo != null && formInfo.length != 0) {
                        commit('historyForms', formInfo)
                        return formInfo;
                    }
                }
            })
    },
    // 重置表单
    resetFormData({ commit }) {
        commit('resetFormData')
        return true;
    },
    init({ dispatch }) {
        return axios.get(SERVER_URL + "/editing-file-id")
            .then(async (res) => {
                var formId = res.data;
                if (res.status == 200) {
                    // 表单列表
                    await dispatch('fetchHistoryForms')
                    // 版本列表
                    var versionList = await dispatch('fetchHistoryVersions', formId)

                    // 表单内容
                    await dispatch('refreshFormContent', {
                        formId: formId,
                        versionId: versionList.length - 1
                    });
                }
                return formId;
            });
    }
}

export default actions