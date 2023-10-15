const mutations = {
    historyVersions(state, payload) {
        state.historyVersions = payload
    },
    historyForms(state, payload) {
        state.historyForms = payload
    },
    refreshForm(state, payload) {
        state.formData = payload.data['form-data'][0]
        state.formJson = payload.data['form-json'][0]
    },
    resetFormData(state) {
        state.formData = {}
        state.formInfo = {}
        state.historyVersions = []
    }
}

export default mutations