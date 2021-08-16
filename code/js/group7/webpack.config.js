const path = require("path");

module.exports = {
  devtool: 'eval-source-map', // https://webpack.js.org/configuration/devtool/ , not for production
  mode: "development",
  entry: "./src/index.ts",
  output: {
    filename: "main.js",
    path: path.resolve(__dirname, "dist"),
  },
  devServer: {
    port: 9000,
    contentBase: "./dist",
    historyApiFallback: true,
    proxy: {
      "/group7api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
  resolve: {
    extensions: [".tsx", ".ts", ".js", "scss"],
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: "ts-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.(jpe?g|png|gif|svg)$/i,
        loader: "file-loader",
        options: {
          name: "./images/[name].[ext]",
        },
      },
    ],
  },
};
