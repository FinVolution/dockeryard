<template>
    <div class="wrapper">
        <v-header></v-header>
        <v-sidebar></v-sidebar>
        <div class="content">
            <transition name="move" mode="out-in">
                <router-view></router-view>
            </transition>
        </div>
    </div>
</template>

<script>
    import vHeader from './Header.vue';
    import vSidebar from './SideBar.vue';
    import {mapGetters} from "vuex";
    export default {
        components:{
            vHeader, vSidebar
        },
        computed: {
            ...mapGetters({
                promptMessage: 'getPromptMessage'
            })
        },
        watch: {
            promptMessage: function (newMessage) {
                //console.log(newMessage);
                if (newMessage.code != null) {
                    if (newMessage.code >= 0) {
                        this.$message.success(newMessage.details);
                    } else {
                        this.$message.error(newMessage.details);
                    }
                }
            }
        }
    }
</script>
