<template>
  <codemirror v-model='data' :options='cmOptions'></codemirror>
</template>

<script>
// require component
import { codemirror } from 'vue-codemirror'

// require styles
import 'codemirror/lib/codemirror.css'

// language js
import 'codemirror/mode/clike/clike.js'

// theme css
import 'codemirror/theme/darcula.css'

export default {
  name: 'Editor',
  /* props: ['code'], */
  components: {
    codemirror
  },
  data () {
    return {
      data: '',
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: 'text/x-csrc',
        theme: 'darcula',
        lineNumbers: true,
        line: true
      }
    }
  },
  methods: {

  }
}
</script>
