var Todo = Backbone.Model.extend({
    // Default todo attribute values
    defaults: {
        title: '',
        description: '',
        start: '',
        status: '',
        end: '',
        important: false,
        username: '',
        created: '',
        completed: false
    },
    initialize: function(){
       console.log('This model has been initialized.');
       this.on('change', function(){
         console.log('- Values for this model have changed.');
       });
       this.on('change:title', function(){
             console.log('Title value for this model has changed.');
       });
    }
});

