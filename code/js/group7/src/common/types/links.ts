//Type for router.tsx to parse the list of links that comes from the /group7api request
export type Links = {
  home: string; ///group7api/home
  allProjects: string; ///group7api/projects?PageIndex={index}&PageSize={pageSize}
  userDetails: string; ///group7api/users/{userid}
  userLogin: string; //"/group7api/users/login"
  userProjects: string; ///group7api/{userid}/projects?PageIndex={index}&PageSize={pageSize}
  userIssues: string; ///group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}
  projectDetails: string; ///group7api/{userid}/projects/{projectid}
  issueDetails: string; ///group7api/{userid}/projects/{projectid}/issues/{issueid}
  issueCollection: string; ///group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}
  issueCommentsCollection: string; ///group7api/{userid}/projects/{projectid}/issues/{issueid}/comments?PageIndex={index}&PageSize={pageSize}
  commentDetails: string; ///group7api/{userid}/projects/{projectid}/issues/{issueid}/comments/{commentid}
  changeLabel: string; ///group7api/{userid}/projects/{projectid}/issues/{issueid}/labels ->same URL for add and delete label
  changeState: string; ///group7api/{userid}/projects/{projectid}/issues/{issueid}/state
};

export type LinksSiren = {
  rel: Array<string>;
  href: string;
};
