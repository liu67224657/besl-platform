function jump(jt, ji) {
    var stringval = 'jt=' + jt + "&ji=" + ji;
    try {
        _jclient.jump(stringval);
    } catch (e) {
    } finally {
        return;
    }
}

function saveLogin(profile, callback) {
    var stringval = 'token=' + profile.token + "&uid=" + profile.uid + "&icon=" + profile.icon + "&uno=" + profile.uno + "&nick=" + profile.nick + "&description=" + profile.description;
    try {
        _jclient.login(stringval);
    } catch (e) {
    } finally {
        callback();
    }
}

function logout(callback) {
    try {
        _jclient.rsAction("rs=-10102&msg=用户信息过期，请重新登录", function () {
        });
    } catch (e) {
    } finally {
        callback();
    }
}

function showLogin() {
    var stringval = '';
    try {
        _jclient.showLogin(stringval);
    } catch (e) {
    } finally {
        return;
    }
}

function getJParam() {
    var JParam = "";
    try {
        JParam = _jclient.getJParam();
    } catch (e) {
        return JParam;
    } finally {
        return JParam;
    }
}