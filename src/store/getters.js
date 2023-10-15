import _ from 'lodash'

const getters = {
    getHistoryForms: (state) => {
        return _.concat([{ id: "new", name: "新建表单", createDate: "", updateDate: "" }], state.historyForms)
    },
    getHistoryVersions: (state) => {
        if (state.historyVersions.length == 0) {
            // eslint-disable-next-line no-debugger
            debugger
            return [{ id: "latest", name: "最新 - 1", formID: "new", version: 0 }]
        }

        var result = state.historyVersions
            .map((obj, index) => {
                if (index == 0) {
                    return { id: obj.id, name: "最新 - " + obj.version, formID: obj.formID, version: obj.version }
                }
                return { id: obj.id, name: "版本 - " + obj.version, formID: obj.formID, version: obj.version }
            })

        return result
    },
    getFormData: (state) => {
        return state.formData
    },
    getFormJson: (state) => {
        return state.formJson
    },
}

export default getters