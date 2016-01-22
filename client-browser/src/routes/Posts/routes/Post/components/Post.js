import React from 'react';

class Post extends React.Component {
  render() {
    //let { courseId, announcementId } = this.props.params
    //let { title, body } = COURSES[courseId].announcements[announcementId]

    return (
      <div>
        <h4>Post ({this.props.params.postId})</h4>
        <p>Lipsum.</p>
      </div>
    );
  }
}
;

module.exports = Post;
