import React from "react";
import { CommentCollection } from "../types/commentCollection";
import { ProjectCollection } from "../types/projectCollection";
import { IssueCollection } from "../types/issueCollection";

/// Page buttons component props
type PageButtonsProps = {
  pageState: { currentPage: number; pageSize: number };
  setPageState({}): void;
  collection: ProjectCollection | CommentCollection | IssueCollection;
};

/// Page buttons component
export function PageButtons( { pageState, setPageState, collection }: PageButtonsProps) {
  
  return (
    <div style={styles.divStyle}>

      {/* Previous button */}
      <button
        className="ui labeled icon button"
        onClick={() => {
          if (pageState.currentPage > 1)
            setPageState({
              currentPage: pageState.currentPage - 1,
              pageSize: pageState.pageSize,
            });
        }}
      >
        <i className="left arrow icon"></i> Previous
      </button>

      {/* Next button */}
      <button
        className="ui right labeled icon button"
        onClick={() => {
          if (pageState.currentPage < collection.properties.totalPages) {
            setPageState({
              currentPage: pageState.currentPage + 1,
              pageSize: pageState.pageSize,
            });
          }
        }}
      >
        <i className="right arrow icon"></i> Next
      </button>
      
    </div>
  );
}

// Styles
const styles = {
  divStyle: {
    margin: "auto",
    width: "60%",
    padding: 10,
    textAlign: "center" as "center",
    paddingTop: "60px",
  },
};
